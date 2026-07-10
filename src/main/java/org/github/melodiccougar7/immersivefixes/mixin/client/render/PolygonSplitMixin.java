package org.github.melodiccougar7.immersivefixes.mixin.client.render;

import malte0811.modelsplitter.math.EpsilonMath;
import malte0811.modelsplitter.math.Plane;
import malte0811.modelsplitter.model.Polygon;
import malte0811.modelsplitter.model.Vertex;
import malte0811.modelsplitter.util.CyclicListWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(value = Polygon.class, remap = false)
public abstract class PolygonSplitMixin<Texture> {
    @Unique private static final EpsilonMath IMMERSIVEFIXES$EPS_MATH = new EpsilonMath(1e-5);

    @Shadow public abstract List<Vertex> getPoints();

    @Shadow public abstract Texture getTexture();

    @Unique @SuppressWarnings("unchecked") private Polygon<Texture> immersiveFixes$self() { return (Polygon<Texture>) (Object) this; }

    /**
     * @author tgstyle
     * @reason Rebuilds both split halves with explicit, order-preserving vertex lists so the
     * boundary edge of the second half always runs otherNewPoint -> firstNewPoint. Older
     * bundled BlockModelSplitter versions assembled the second half with swapped endpoints,
     * producing self-intersecting polygons that quadified into partially inverted faces.
     */
    @Overwrite
    public Map<EpsilonMath.Sign, Polygon<Texture>> splitAlong(Plane p) {
        List<Vertex> points = getPoints();
        List<EpsilonMath.Sign> signs = new ArrayList<>(points.size());
        for (Vertex point : points) {
            double product = p.normal().dotProduct(point.position()) - p.dotProduct();
            signs.add(IMMERSIVEFIXES$EPS_MATH.sign(product));
        }
        int firstSignStart = 0;
        EpsilonMath.Sign zeroSign = signs.get(0);
        for (; firstSignStart < points.size(); ++firstSignStart) {
            EpsilonMath.Sign signHere = signs.get(firstSignStart);
            if (zeroSign != signHere && signHere != EpsilonMath.Sign.ZERO) { break; }
        }
        if (firstSignStart >= points.size()) { return Map.of(zeroSign, immersiveFixes$self()); }
        EpsilonMath.Sign firstSign = signs.get(firstSignStart);
        EpsilonMath.Sign otherSign = firstSign.invert();
        if (!signs.contains(otherSign)) { return Map.of(firstSign, immersiveFixes$self()); }
        CyclicListWrapper<EpsilonMath.Sign> cyclicSigns = new CyclicListWrapper<>(signs);
        CyclicListWrapper<Vertex> cyclicPoints = new CyclicListWrapper<>(points);
        int otherSignStart = firstSignStart;
        while (cyclicSigns.get(otherSignStart) != otherSign) { ++otherSignStart; }
        List<Vertex> firstInnerPoints = cyclicPoints.sublist(firstSignStart, otherSignStart);
        List<Vertex> otherInnerPoints = cyclicPoints.sublist(otherSignStart, firstSignStart);
        Vertex firstNewPoint = immersiveFixes$intersect(cyclicPoints.get(firstSignStart - 1), cyclicPoints.get(firstSignStart), p);
        Vertex otherNewPoint = immersiveFixes$intersect(cyclicPoints.get(otherSignStart - 1), cyclicPoints.get(otherSignStart), p);
        List<Vertex> poly1 = new ArrayList<>(firstInnerPoints.size() + 2);
        poly1.add(firstNewPoint);
        poly1.addAll(firstInnerPoints);
        poly1.add(otherNewPoint);
        List<Vertex> poly2 = new ArrayList<>(otherInnerPoints.size() + 2);
        poly2.add(otherNewPoint);
        poly2.addAll(otherInnerPoints);
        poly2.add(firstNewPoint);
        return Map.of(
                firstSign, new Polygon<>(poly1, getTexture()),
                otherSign, new Polygon<>(poly2, getTexture())
        );
    }

    @Unique private Vertex immersiveFixes$intersect(Vertex a, Vertex b, Plane p) {
        double productA = a.position().dotProduct(p.normal());
        double productB = b.position().dotProduct(p.normal());
        double lambda = (p.dotProduct() - productB) / (productA - productB);
        return Vertex.interpolate(a, b, lambda);
    }
}
