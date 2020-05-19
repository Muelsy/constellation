/*
 * Copyright 2010-2020 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.visual.opengl.renderer.batcher;

import au.gov.asd.tac.constellation.utilities.graphics.Vector3f;
import au.gov.asd.tac.constellation.utilities.graphics.Vector4f;
import au.gov.asd.tac.constellation.visual.opengl.utilities.RenderException;
import au.gov.asd.tac.constellation.visual.opengl.utilities.ShaderManager;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.GLBuffers;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Batch is a class that collects a number of buffers of various types of data
 * that can be populated, updated and passed through a shader as one easily
 * managed entity.
 * <p>
 *
 * The life-cycle of a batch <code>b</code>, illustrating how to construct it
 * and then use it for rendering, is as follows:
 * <pre>
 *
 * // Create the buffers required
 * target = b.newFloatBuffer(valuesPerVertex)
 *
 * // Initialise the batch
 * b.initialise(size)
 *
 * // Buffer data (on the GL context or locally)
 * b.buffer(gl, target, data) | b.stage(target, data)
 *
 * // Finalise the batch
 * b.finalise()
 *
 * // Update a buffer on the batch directly on the GL context.
 * data = b.connectFloatBuffer(gl, target)
 * ...
 * b.disconnectBuffer(gl, target)
 *
 * // Draw the batch on the GL context. Updates and draws may be interspersed.
 * b.draw(gl)
 *
 * // Dispose of the batch on the GL context.
 * b.dispose(gl)
 * </pre>
 * <p>
 * Note that "target" is a constant created by {@link ShaderManager} denoting a
 * particular target buffer on the card (NOT a buffer name generated by GL
 * itself; these are handled by the batch internally)
 * <p>
 * Essentially there are three ways to get onto the card using the life-cycle
 * above:
 * <ol>
 * <li>stage data to this Batch's local buffers which will buffered on the GL
 * context once <code>finalise()</code> is called. When this occurs, the size of
 * all staged data must match the size of the corresponding target buffers
 * exactly.
 * </li><li>call buffer to buffer chunks of data on the GL context. If an offset
 * is supplied, the length of the data plus the offset must not exceed the
 * target buffer size. With no offset, the size of the data must match the size
 * of the target exactly.
 * </li><li>Connect to the desired buffer, modify it at will, and then
 * disconnect from it. This is, in essence, directly modifying a buffer shared
 * with the GL context, and hence should be used with caution.
 * </ol>
 * <p>
 * All three methods may be combined, although between initialise and finalise,
 * only one of buffer/stage should be used per target. Note also that the call
 * to <code>finalise()</code> will wipe any locally staged data after passing it
 * to the GL context.
 *
 * @author twilight_sparkle
 */
public final class Batch {

    private static final Logger LOGGER = Logger.getLogger(Batch.class.getName());
    
    private static final String NOT_FLOATBUFFER = "Specified target is not a FloatBuffer";

    // What am I drawing?
    private final int primitiveType;

    private int[] vertexArrayObjectName;    // length 1

    private final int[][] bufferNames;
    private final boolean[] bufferIsFloat;
    private final boolean[] bufferIsLocal;
    private final int[] bufferSizePerVertex;
    private final Buffer[] buffers; // local buffers - may or may not be used.

    private int numVertices;                  // Number of vertices in this batch
    private boolean finalised = false;              // Batch has been built
    private boolean initialised = false;              // Batch has been initialised and can accpet data
    private int numBuffers = 0;

    /**
     * Creates a new Batch for the specified GL primitive type.
     *
     * @param primitive A primitive drawing type: see OpenGL 3.3 section 2.6.1
     * and the definition of DrawArrays.
     */
    public Batch(final int primitive) {

        this.primitiveType = primitive;
        bufferNames = new int[ShaderManager.ATTRIBUTE_LAST][];
        bufferIsFloat = new boolean[ShaderManager.ATTRIBUTE_LAST];
        bufferIsLocal = new boolean[ShaderManager.ATTRIBUTE_LAST];
        bufferSizePerVertex = new int[ShaderManager.ATTRIBUTE_LAST];
        buffers = new Buffer[ShaderManager.ATTRIBUTE_LAST];
        Arrays.fill(bufferNames, new int[0]);
    }

    /**
     * Create a new Batch with the same buffer specifications as the specified
     * batch.
     * <p>
     * This only works pre-initialisation: after initialisation batches depend
     * on the specific GLContext with which they were initialised.
     *
     * @param other The Batch to model the new Batch on.
     */
    public Batch(final Batch other) {
        if (other.initialised) {
            throw new RenderException("Cant copy batch structure from a batch that has already been initialised, as it may be dependent on a GL context.");
        }
        primitiveType = other.primitiveType;
        numBuffers = other.numBuffers;
        bufferIsFloat = Arrays.copyOf(other.bufferIsFloat, other.bufferIsFloat.length);
        bufferIsLocal = Arrays.copyOf(other.bufferIsLocal, other.bufferIsLocal.length);
        bufferSizePerVertex = Arrays.copyOf(other.bufferSizePerVertex, other.bufferSizePerVertex.length);

        bufferNames = new int[ShaderManager.ATTRIBUTE_LAST][];
        buffers = new Buffer[ShaderManager.ATTRIBUTE_LAST];
        Arrays.fill(bufferNames, new int[0]);
    }

    /**
     * Create a new integer buffer.
     *
     * @param valuesPerVertex The number of ints to buffer for each vertex
     * @param local Whether or not data for this buffer can be staged locally
     * (rather than being sent directly to the card).
     * @return An ID for the buffer that can be used after initialisation for
     * the purpose of buffering data.
     */
    public int newIntBuffer(final int valuesPerVertex, final boolean local) {
        return newBuffer(valuesPerVertex, local, false);
    }

    /**
     * Create a new float buffer.
     *
     * @param valuesPerVertex The number of floats to buffer for each vertex
     * @param local Whether or not data for this buffer can be staged locally
     * (rather than being sent directly to the card).
     * @return An ID for the buffer that can be used after initialisation for
     * the purpose of buffering data.
     */
    public int newFloatBuffer(final int valuesPerVertex, final boolean local) {
        return newBuffer(valuesPerVertex, local, true);
    }

    private int newBuffer(final int valuesPerVertex, final boolean local, final boolean isFloat) {
        if (initialised) {
            throw new RenderException("Can't add new buffers once a batch has been initialised.");
        }
        if (numBuffers == bufferNames.length) {
            throw new RenderException("Maximum number of buffers added");
        }
        bufferIsFloat[numBuffers] = isFloat;
        bufferSizePerVertex[numBuffers] = valuesPerVertex;
        bufferIsLocal[numBuffers] = local;
        return numBuffers++;
    }

    /**
     * Initialise this batch, setting up the specified buffer information for
     * the specified number of vertices.
     *
     * @param numVertices The number of vertices that this batch should buffer
     * and draw for.
     */
    public void initialise(final int numVertices) {
        if (finalised) {
            throw new RenderException("Can't initialise once a batch has been finalised on a GL context. Dispose it first.");
        }
        this.numVertices = numVertices;
        for (int i = 0; i < bufferIsLocal.length; i++) {
            if (bufferIsLocal[i]) {
                if (bufferIsFloat[i]) {
                    buffers[i] = Buffers.newDirectFloatBuffer(numVertices * bufferSizePerVertex[i]);
                } else {
                    buffers[i] = Buffers.newDirectIntBuffer(numVertices * bufferSizePerVertex[i]);
                }
            }
        }
        initialised = true;
    }

    /**
     * Stage data locally to the specified float buffer.
     *
     * @param target The float buffer to stage to.
     * @param data The data to stage
     */
    public void stage(final int target, final float... data) {
        if (buffers[target] == null || !bufferIsFloat[target]) {
            throw new RenderException("Can't stage data to the the specified target buffer: either it is not a float buffer, not local, or the batch has not been initialised.");
        }
        ((FloatBuffer) buffers[target]).put(data);
    }

    /**
     * Stage data locally to the specified int buffer.
     *
     * @param target The int buffer to stage to.
     * @param data The data to stage
     */
    public void stage(final int target, final int... data) {
        if (buffers[target] == null || bufferIsFloat[target]) {
            throw new RenderException("Can't stage data to the the specified target buffer: either it is not an int buffer, not local, or the batch has not been initialised.");
        }
        ((IntBuffer) buffers[target]).put(data);
    }

    /**
     * Stage a 3D vector locally to the specified float buffer.
     *
     * @param target The float buffer to stage to.
     * @param data The vector to stage
     */
    public void stage(final int target, final Vector3f data) {
        stage(target, data.getX(), data.getY(), data.getZ());
    }

    /**
     * Stage a 4D vector locally to the specified float buffer.
     *
     * @param target The float buffer to stage to.
     * @param data The vector to stage
     */
    public void stage(final int target, final Vector4f data) {
        stage(target, data.getX(), data.getY(), data.getZ(), data.getW());
    }

    private int getOrCreateBufferName(final GL3 gl, final int target) {
        try {
            if (bufferNames[target].length == 0) {
                bufferNames[target] = new int[1];
                gl.glGenBuffers(1, bufferNames[target], 0);
            }
            return bufferNames[target][0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new RenderException("Specified target doesn't exist.");
        }
    }

    /**
     * Buffer data from an explicitly provided buffer to a float buffer in this
     * batch.
     * <p>
     * If the target buffer doesn't yet exist on the GL context, it will be
     * created.
     *
     * @param gl The GL context on which to buffer the data.
     * @param target The float buffer to buffer to
     * @param buffer The data to buffer
     */
    public void buffer(final GL3 gl, final int target, final FloatBuffer buffer) {
        if (!bufferIsFloat[target]) {
            throw new RenderException(NOT_FLOATBUFFER);
        }
        final int size = bufferSizePerVertex[target] * numVertices;
        bufferData(gl, target, size, GLBuffers.SIZEOF_FLOAT, buffer);
    }

    /**
     * Buffer data from an explicitly provided buffer to a float buffer in this
     * batch, starting at the given offset.
     * <p>
     * If the target buffer doesn't yet exist on the GL context, it will be
     * created.
     *
     * @param gl The GL context on which to buffer the data.
     * @param target The float buffer to buffer to
     * @param buffer The data to buffer
     * @param offset the offset at which to begin the buffering operation.
     */
    public void buffer(final GL3 gl, final int target, final FloatBuffer buffer, final int offset) {
        if (!bufferIsFloat[target]) {
            throw new RenderException(NOT_FLOATBUFFER);
        }
        final int sizeLimit = bufferSizePerVertex[target] * numVertices;

        bufferSubData(gl, target, offset, sizeLimit, GLBuffers.SIZEOF_FLOAT, buffer);
    }

    /**
     * Buffer data from an explicitly provided buffer to an int buffer in this
     * batch.
     * <p>
     * If the target buffer doesn't yet exist on the GL context, it will be
     * created.
     *
     * @param gl The GL context on which to buffer the data.
     * @param target The int buffer to buffer to
     * @param buffer The data to buffer
     */
    public void buffer(final GL3 gl, final int target, final IntBuffer buffer) {
        if (bufferIsFloat[target]) {
            throw new RenderException("Specified target is not an IntBuffer");
        }
        final int size = bufferSizePerVertex[target] * numVertices;
        bufferData(gl, target, size, GLBuffers.SIZEOF_INT, buffer);
    }

    /**
     * Buffer data from an explicitly provided buffer to a int buffer in this
     * batch, starting at the given offset.
     * <p>
     * If the target buffer doesn't yet exist on the GL context, it will be
     * created.
     *
     * @param gl The GL context on which to buffer the data.
     * @param target The int buffer to buffer to
     * @param buffer The data to buffer
     * @param offset
     */
    public void buffer(final GL3 gl, final int target, final IntBuffer buffer, final int offset) {
        if (bufferIsFloat[target]) {
            throw new RenderException("Specified target is not a IntBuffer");
        }
        final int sizeLimit = bufferSizePerVertex[target] * numVertices;

        bufferSubData(gl, target, offset, sizeLimit, GLBuffers.SIZEOF_INT, buffer);
    }

    private void bufferData(final GL3 gl, final int target, final int size, final int itemSize, final Buffer buffer) {
        if (!initialised) {
            throw new RenderException("Can't buffer data before initialising the batch");
        }
        final int bufferName = getOrCreateBufferName(gl, target);
        if (size != (buffer.limit() - buffer.position())) {
            throw new RenderException("Incorrect amount of data for the specified target "
                    + "[size=" + size + ", limit=" + buffer.limit() + ", position=" + buffer.position() + "]");
        }
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, bufferName);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, size * itemSize, buffer, GL3.GL_DYNAMIC_DRAW);
    }

    private void bufferSubData(final GL3 gl, final int target, final int offset, final int sizeLimit, final int itemSize, final Buffer buffer) {
        if (!initialised) {
            throw new RenderException("Can't buffer data before initialising the batch");
        }
        final int size = buffer.limit() - buffer.position();
        if (offset + size > sizeLimit) {
            throw new RenderException("Data size exceeds size of the specified target");
        }
        final int bufferName = getOrCreateBufferName(gl, target);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, bufferName);
        gl.glBufferSubData(GL3.GL_ARRAY_BUFFER, offset * itemSize, size * itemSize, buffer);
    }

    /**
     * Finalise this batch with the supplied GL context, allowing it to be drawn
     * using this context.
     * <p>
     * This will transfer any locally staged data to buffers on the GL context,
     * then wipe the local data.
     *
     * @param gl The GL context to finalise this batch on.
     */
    public void finalise(final GL3 gl) {
        if (!initialised) {
            throw new RenderException("Can't finalise the batch before initialising");
        }
        for (int target = 0; target < numBuffers; target++) {
            if (buffers[target] != null) {
                final int size = bufferSizePerVertex[target] * numVertices;
                bufferData(gl, target, size, bufferIsFloat[target] ? GLBuffers.SIZEOF_FLOAT : GLBuffers.SIZEOF_INT, buffers[target].flip());
            }
        }

        vertexArrayObjectName = new int[1];
        gl.glGenVertexArrays(1, vertexArrayObjectName, 0);
        gl.glBindVertexArray(vertexArrayObjectName[0]);
        for (int target = 0; target < numBuffers; target++) {
            // Set up the vertex array object.
            gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, getBufferName(target));
            if (bufferIsFloat[target]) {
                gl.glVertexAttribPointer(target, bufferSizePerVertex[target], GL3.GL_FLOAT, false, 0, 0);
            } else {
                gl.glVertexAttribIPointer(target, bufferSizePerVertex[target], GL3.GL_INT, 0, 0L);
            }
        }

        gl.glBindVertexArray(0);
        finalised = true;
    }

    /**
     * Get the OpenGL buffer name for the specified (finalised) buffer.
     * <p>
     * This is provided for clients that want to manually bind to and alter the
     * buffer themselves. This is generally discouraged but left here for
     * flexibility and backwards compatiblity for older code.
     * <p>
     * Note that the returned name will obviously only be correct for the GL
     * context on which the buffer was finalised.
     *
     * @param target The buffer to get the OpenGL name for.
     * @return The OpenGL name for the specified buffer.
     */
    public int getBufferName(final int target) {
        try {
            return bufferNames[target][0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new RenderException("Specified target doesn't exist or its buffer is not yet created.");
        }
    }

    /**
     * Connect to a finalised float buffer on a GL Context for the purpose of
     * updating its values directly.
     *
     * @param gl The GLContext on which the buffer was finalised.
     * @param target
     * @return A FloatBuffer directly backed by data on the GL context.
     */
    public FloatBuffer connectFloatBuffer(final GL3 gl, final int target) {
        final int bufferName = getBufferName(target);
        if (!bufferIsFloat[target]) {
            throw new RenderException(NOT_FLOATBUFFER);
        }
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, bufferName);
        final ByteBuffer connectionBbuf = gl.glMapBuffer(GL3.GL_ARRAY_BUFFER, GL3.GL_READ_WRITE);
        return connectionBbuf.order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    /**
     * Connect to a finalised int buffer on a GL Context for the purpose of
     * updating its values directly.
     *
     * @param gl The GLContext on which the buffer was finalised.
     * @param target
     * @return An IntBuffer directly backed by data on the GL context.
     */
    public IntBuffer connectIntBuffer(final GL3 gl, final int target) {
        final int bufferName = getBufferName(target);
        if (bufferIsFloat[target]) {
            throw new RenderException("Specified target is not an IntBuffer");
        }
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, bufferName);
        final ByteBuffer connectionBbuf = gl.glMapBuffer(GL3.GL_ARRAY_BUFFER, GL3.GL_READ_WRITE);
        return connectionBbuf.order(ByteOrder.nativeOrder()).asIntBuffer();
    }

    /**
     * Disconnect from a finalised buffer.
     * <p>
     * This should be called after {@link #connectIntBuffer} or
     * {@link #connectFloatBuffer}, prior to a call to {@link #draw}.
     *
     * @param gl The GL context on which the buffer was finalised.
     * @param target The buffer to disconnect.
     */
    public void disconnectBuffer(final GL3 gl, final int target) {
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, getBufferName(target));
        gl.glUnmapBuffer(GL3.GL_ARRAY_BUFFER);
    }

    /**
     * Draw the contents of this batch using the specified open GL context.
     * <p>
     * This assumes that all the relevant shader loading and binding of uniforms
     * has been done immediately prior to this call on the specified GL context.
     *
     * @param gl The GL context to draw the batch to.
     */
    public void draw(final GL3 gl) {
        if (!finalised) {
            throw new RenderException("Attempting to draw this batch before first finalising it on the relevant open GL context.");
        }

        // glDrawArrays() throws INVALID_OPERATION on some video cards when using texture buffers.
        // Catch it here to avoid problems in other areas.
        try {
            gl.glBindVertexArray(vertexArrayObjectName[0]);

            enableVertexAttribArrays(gl);

            gl.glDrawArrays(primitiveType, 0, numVertices);

            disableVertexAttribArrays(gl);

            gl.glBindVertexArray(0);
        } catch (GLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Dispose of this batch from the specified GL context.
     *
     * @param gl The GL context to dispose this batch from.
     */
    public void dispose(final GL3 gl) {
        for (int i = 0; i < bufferNames.length; i++) {
            if (bufferNames[i].length != 0) {
                gl.glDeleteBuffers(1, bufferNames[i], 0);
                bufferNames[i] = new int[0];
            }
            if (bufferIsLocal[i]) {
                buffers[i] = null;
            }
        }

        numVertices = 0;
        if (finalised) {
            gl.glDeleteVertexArrays(1, vertexArrayObjectName, 0);
        }
        finalised = false;
        initialised = false;
    }

    /**
     * @return Whether or not the batch is currently in a state that can be
     * drawn on an openGL context by calling {@link #draw}.
     */
    public boolean isDrawable() {
        return numVertices > 0 && finalised;
    }

    /**
     * Enable all the vertex attribute arrays. The arrays should be enabled and
     * disabled each draw routine because this state is global and will affect
     * other draws.
     *
     * @param gl
     */
    private void enableVertexAttribArrays(final GL3 gl) {
        for (int target = 0; target < numBuffers; target++) {
            try {
                getBufferName(target);
                gl.glEnableVertexAttribArray(target);
            } catch (RenderException ex) {
            }
        }
    }

    /**
     * Disable all the vertex attribute arrays. The arrays should be enabled and
     * disabled each draw routine because this state is global and will affect
     * other draws.
     *
     * @param gl
     */
    private void disableVertexAttribArrays(final GL3 gl) {
        for (int target = 0; target < numBuffers; target++) {
            try {
                getBufferName(target);
                gl.glDisableVertexAttribArray(target);
            } catch (RenderException ex) {
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Batch[@%d, primitive=%d,vertices=%d]", hashCode(), primitiveType, numVertices);
    }
}
