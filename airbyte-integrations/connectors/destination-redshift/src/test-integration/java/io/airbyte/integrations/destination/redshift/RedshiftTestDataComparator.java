package io.airbyte.integrations.destination.redshift;

import io.airbyte.integrations.standardtest.destination.comparator.AdvancedTestDataComparator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RedshiftTestDataComparator extends AdvancedTestDataComparator {

    private final RedshiftSQLNameTransformer namingResolver = new RedshiftSQLNameTransformer();

    protected static final String REDSHIFT_DATETIME_WITH_TZ_FORMAT = "yyyy-MM-dd HH:mm:ssX";

    @Override
    protected ZonedDateTime parseDestinationDateWithTz(String destinationValue) {
        return ZonedDateTime.parse(destinationValue, DateTimeFormatter.ofPattern(REDSHIFT_DATETIME_WITH_TZ_FORMAT)).withZoneSameInstant(ZoneOffset.UTC);
    }

    @Override
    protected boolean compareDateTimeValues(String airbyteMessageValue, String destinationValue) {
        var format = DateTimeFormatter.ofPattern(AIRBYTE_DATETIME_FORMAT);
        LocalDateTime dateTime = LocalDateTime.parse(destinationValue, DateTimeFormatter.ofPattern(REDSHIFT_DATETIME_WITH_TZ_FORMAT));
        return super.compareDateTimeValues(airbyteMessageValue, format.format(dateTime));
    }

    @Override
    protected List<String> resolveIdentifier(final String identifier) {
        final List<String> result = new ArrayList<>();
        final String resolved = namingResolver.getIdentifier(identifier);
        result.add(identifier);
        result.add(resolved);
        if (!resolved.startsWith("\"")) {
            result.add(resolved.toLowerCase());
            result.add(resolved.toUpperCase());
        }
        return result;
    }
}
