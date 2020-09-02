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
package au.gov.asd.tac.constellation.graph.value.operations;

import au.gov.asd.tac.constellation.graph.value.ArithmeticOperation;
import au.gov.asd.tac.constellation.graph.value.Operators;
import au.gov.asd.tac.constellation.graph.value.readables.DoubleReadable;
import au.gov.asd.tac.constellation.graph.value.readables.FloatReadable;
import au.gov.asd.tac.constellation.graph.value.readables.IntReadable;
import au.gov.asd.tac.constellation.graph.value.readables.LongReadable;

/**
 *
 * @author sirius
 */
public class Quotient {
    
    public static final String NAME = new String("QUOTIENT");
    
    public static final ArithmeticOperation ARITHMETIC_OPERATION = new ArithmeticOperation() {
        @Override
        public double execute(double p1, double p2) {
            return p1 / p2;
        }

        @Override
        public float execute(float p1, float p2) {
            return p1 / p2;
        }

        @Override
        public long execute(long p1, long p2) {
            return p1 / p2;
        }

        @Override
        public int execute(int p1, int p2) {
            return p1 / p2;
        }
    };
    
    public static void register(Operators operators) {
        final var registry = operators.getRegistry(NAME);
        ARITHMETIC_OPERATION.register(registry);
    }
    
    static {
        register(Operators.getDefault());
    }
}