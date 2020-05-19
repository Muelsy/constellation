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
package au.gov.asd.tac.constellation.graph.attribute;

import au.gov.asd.tac.constellation.utilities.temporal.TemporalConstants;
import au.gov.asd.tac.constellation.utilities.temporal.TemporalFormatting;
import au.gov.asd.tac.constellation.utilities.temporal.TimeZoneUtilities;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.openide.util.lookup.ServiceProvider;

/**
 * A description of a datetime attribute.
 *
 * @author sirius
 */
@ServiceProvider(service = AttributeDescription.class)
public final class ZonedDateTimeAttributeDescription extends AbstractObjectAttributeDescription<ZonedDateTime> {

    public static final String ATTRIBUTE_NAME = "datetime";
    public static final int ATTRIBUTE_VERSION = 1;
    public static final Class<ZonedDateTime> NATIVE_CLASS = ZonedDateTime.class;
    public static final ZonedDateTime DEFAULT_VALUE = null;

    public ZonedDateTimeAttributeDescription() {
        super(ATTRIBUTE_NAME, NATIVE_CLASS, DEFAULT_VALUE);
    }

    @Override
    public int getVersion() {
        return ATTRIBUTE_VERSION;
    }
    
    @Override
    public ZonedDateTime convertFromObject(final Object object) {
        try {
            return super.convertFromObject(object);
        } catch (final IllegalArgumentException ex) {
            if (object instanceof Date) {
                return ZonedDateTime.ofInstant(Instant.ofEpochMilli(((Date) object).getTime()), TimeZoneUtilities.UTC);
            } else if (object instanceof Calendar) {
                return ZonedDateTime.ofInstant(Instant.ofEpochMilli(((Calendar) object).getTimeInMillis()), ((Calendar) object).getTimeZone().toZoneId());
            } else if (object instanceof Number) {
                return ZonedDateTime.ofInstant(Instant.ofEpochMilli(((Number) object).longValue()), TimeZoneUtilities.UTC);
            } else {
                throw new IllegalArgumentException(String.format(
                        "Error converting Object '%s' to datetime", object.getClass()));
            }
        }
    }

    /**
     * Parse a string in CONSTELLATION's date-time format to a ZonedDateTime.
     *
     * CONSTELLATION's date-time format is that which would be generated by
     * {@link TemporalFormatting#ZONED_DATE_TIME_FORMATTER}.
     * <p>
     * Strings using Z as a zone (yyyy-MM-ddTHH:mm:ss.SSSZ) are also accepted.
     * (No checking is done: Other letters than Z are also accepted, but are
     * read as UTC.)
     * <p>
     * Parsing isn't strict: the date 2011-99-01 will be accepted and
     * punctuation isn't checked. Since the context is parsing of dates from
     * CONSTELLATION files, this isn't expected to be a problem. However, this
     * should not be taken as an excuse to write syntactically incorrect
     * datetime strings elsewhere.
     * <p>
     * Note that this method directly reads substrings with static indices, as
     * this is significantly faster than using a ZonedDateTimeFormatter.
     *
     * @param string A String in CONSTELLATION's date-time format
     * (yyyy-MM-ddTHH:mm:ss.SSS[zone]) to be parsed.
     *
     * @return A {@link ZonedDateTime} representing the input datetime.
     */
    @Override
    public ZonedDateTime convertFromString(final String string) {
        if (StringUtils.isBlank(string)) {
            return getDefault();
        } else {
            try {
                final int year = Integer.parseInt(string.substring(0, TemporalFormatting.YEAR_FORMAT_LENGTH), 10);
                final int month = Integer.parseInt(string.substring(TemporalFormatting.MONTH_FOMART_START_POSITION, TemporalFormatting.YEAR_MONTH_FORMAT_LENGTH), 10);
                final int day = Integer.parseInt(string.substring(TemporalFormatting.DAY_FOMART_START_POSITION, TemporalFormatting.DATE_FORMAT_LENGTH), 10);
                final int hour = Integer.parseInt(string.substring(TemporalFormatting.HOUR_FOMART_START_POSITION, TemporalFormatting.DATE_HOUR_FORMAT_LENGTH), 10);
                final int minute = Integer.parseInt(string.substring(TemporalFormatting.DATE_MINUTE_FORMAT_START_POSITION, TemporalFormatting.DATE_HOUR_MINUTE_FORMAT_LENGTH), 10);
                final int second = Integer.parseInt(string.substring(TemporalFormatting.DATE_SECOND_FORMAT_START_POSITION, TemporalFormatting.DATE_HMS_FORMAT_LENGTH), 10);
                final int millisecond = Integer.parseInt(string.substring(TemporalFormatting.DATE_MILLISECOND_FORMAT_START_POSITION, TemporalFormatting.DATE_TIME_FORMAT_LENGTH), 10);
                final String offsetId = string.length() >= TemporalFormatting.ZONE_OFFSET_DATE_TIME_FORMAT_LENGTH
                        ? string.substring(TemporalFormatting.ZONE_OFFSET_FORMAT_START_POSITION, TemporalFormatting.ZONE_OFFSET_DATE_TIME_FORMAT_LENGTH)
                        : null;
                final String regionId = string.length() > TemporalFormatting.ZONE_OFFSET_DATE_TIME_FORMAT_LENGTH
                        ? string.substring(TemporalFormatting.ZONE_NAME_FORMAT_START_POSITION, string.length() - 1)
                        : null;
                final ZoneId zoneId = regionId == null
                        ? offsetId == null
                                ? TimeZoneUtilities.UTC
                                : ZoneOffset.of(offsetId)
                        : ZoneId.of(regionId);
                return ZonedDateTime.of(year, month, day, hour, minute, second, millisecond * TemporalConstants.NANOSECONDS_IN_MILLISECOND, zoneId);
            } catch (final StringIndexOutOfBoundsException | NumberFormatException | DateTimeException ex) {
                throw new IllegalArgumentException(String.format(
                        "Error converting String '%s' to datetime (expected yyyy-MM-ddTHH:mm:ss.SSS[zone])", string), ex);
            }
        }
    }

    @Override
    public long getLong(final int id) {
        return data[id] != null ? ((ZonedDateTime) data[id]).toEpochSecond() * TemporalConstants.MILLISECONDS_IN_SECOND : 0L;
    }

    @Override
    public void setLong(final int id, final long value) {
        data[id] = ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), TimeZoneUtilities.UTC);
    }

    @Override
    public String getString(final int id) {
        return data[id] != null ? ((ZonedDateTime) data[id]).format(TemporalFormatting.ZONED_DATE_TIME_FORMATTER) : null;
    }

    @Override
    public int hashCode(final int id) {
        return data[id] == null ? 0 : ((ZonedDateTime) data[id]).toInstant().hashCode();
    }
}
