
package Graphics;

interface Evaluatable3D {
    float evaluate(float x, float y, float z);
    default float3 deriv(float cx, float cy, float cz) {
        float e = 0.01f;
        float c = evaluate(cx, cy, cz);
        return new float3(
                evaluate(cx+e, cy, cz) - c,
                evaluate(cx, cy+e, cz) - c,
                evaluate(cx, cy, cz+e) - c
        ).normalize();
    };
}