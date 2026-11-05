package id.ac.polban.pbo.shape;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ShapeTest {

    @Test
    void circle_luasDanKeliling() {
        Shape c = new Circle(7);
        assertEquals(153.938, c.area(), 0.001);
        assertEquals(43.982, c.perimeter(), 0.001);
    }

    @Test
    void rectangle_luasDanKeliling() {
        Shape r = new Rectangle(4, 5);
        assertEquals(20, r.area(), 1e-9);
        assertEquals(18, r.perimeter(), 1e-9);
    }

    @Test
    void totalLuas_polimorfik() {
        List<Shape> shapes = List.of(new Circle(1), new Rectangle(2, 3));
        double total = shapes.stream().mapToDouble(Shape::area).sum();
        assertEquals(Math.PI + 6, total, 1e-9);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -2.5})
    void circle_jariJariTidakValid(double r) {
        assertThrows(IllegalArgumentException.class, () -> new Circle(r));
    }
}