package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class StorkAttributesList implements Serializable {
    public static StorkAttributesList EMPTY = new EmptyTypeOfStorkAttributesList();

    private static class EmptyTypeOfStorkAttributesList extends StorkAttributesList {
        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        @Override
        public Set<StorkAttributeType> getTypes() {
            return Collections.emptySet();
        }

        @Override
        public String toString() {
            return "";
        }
    }

    private final Set<StorkAttributeType> types = new HashSet<StorkAttributeType>();

    private StorkAttributesList() {

    }

    private StorkAttributesList(final String types) {
        super();
        this.types.addAll(convertToSet(types));
    }

    public StorkAttributesList(Collection<StorkAttributeType> types) {
        super();
        this.types.addAll(types);
    }

    public Set<StorkAttributeType> getTypes() {
        return Collections.unmodifiableSet(types);
    }

    @Override
    public String toString() {
        return convertToString(this.types);
    }

    private String convertToString(Set<StorkAttributeType> types) {
        final StringBuilder result = new StringBuilder();

        for (StorkAttributeType each : types) {
            result.append(each.name()).append(",");
        }

        if (result.length() > 0 && result.charAt(result.length() - 1) == ',') {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }

    private Set<StorkAttributeType> convertToSet(String types) {
        final Set<StorkAttributeType> result = new HashSet<StorkAttributeType>();

        for (final String each : types.split(",")) {
            String valueToParse = each.trim();
            if (!StringUtils.isEmpty(valueToParse)) {
                result.add(StorkAttributeType.valueOf(valueToParse));
            }
        }

        return result;
    }

    static public StorkAttributesList importFromString(String value) {
        return StringUtils.isEmpty(value) ? EMPTY : new StorkAttributesList(value);
    }

}
