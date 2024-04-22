package watchdogagent.watch;

import lombok.Data;
import org.apache.tools.ant.types.selectors.SelectorUtils;

import java.io.File;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Data
public class PathIdentity {
    private String dirPath;
    private String normalizeDirPath;
    private String file;
    private String[] include;
    private String[] exclude;

    private String normalizePattern(final String p) {
        String pattern = p.replace('/', File.separatorChar)
                .replace('\\', File.separatorChar);
        if (pattern.endsWith(File.separator)) {
            pattern += SelectorUtils.DEEP_TREE_MATCH;
        }
        return pattern;
    }

    @Deprecated
    public PathIdentity(String dirPath, String file) {
        this.dirPath = dirPath;
        this.file = file;
    }

    /**
     * Include has higher priority than exclude. If include is given, exclude is ignored.
     *
     * @param dirPath monitor dir path
     * @param include matcher for files to be included
     * @param exclude matcher for files to be excluded
     */
    public PathIdentity(String dirPath, String[] include, String[] exclude) {
        this.dirPath = dirPath;
        this.normalizeDirPath = dirPath.replace('/', File.separatorChar)
                .replace('\\', File.separatorChar);
        this.include = include == null ? null : Stream.of(include)
                .map(this::normalizePattern).toArray(String[]::new);
        this.exclude = exclude == null ? null : Stream.of(exclude)
                .map(this::normalizePattern).toArray(String[]::new);
    }


    public boolean in(String target) {
        if (this.include != null && this.include.length > 0) {
            return Stream.of(include).anyMatch(i -> SelectorUtils.matchPath(normalizeDirPath + File.separator + i, target));
        } else if (this.exclude != null && this.exclude.length > 0) {
            return Stream.of(exclude).noneMatch(i -> SelectorUtils.matchPath(normalizeDirPath + File.separator + i, target));
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        PathIdentity other = (PathIdentity) obj;
        return Objects.equals(dirPath, other.dirPath);
    }
}
