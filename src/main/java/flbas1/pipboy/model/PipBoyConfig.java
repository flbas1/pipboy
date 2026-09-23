package flbas1.pipboy.model;
import java.util.List;

public record PipBoyConfig(
        List<Tab> tabs
) {
    public record Tab(
            String id,
            String label,
            List<MenuItem> menu
    ) {}

    public record MenuItem(
            String id,
            String label
    ) {}
}