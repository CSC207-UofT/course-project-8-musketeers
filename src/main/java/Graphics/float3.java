package Graphics;

// For compatibility with OpenGL and OpenCL use lowercase.
public class float3 {
    public float x;
    public float y;
    public float z;
    public float3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public float3(float a) {
        this.x = a;
        this.y = a;
        this.z = a;
    }
    public float3 normalize() {
        float d = (float)Math.sqrt(x*x+y*y+z*z);
        return new float3(x/d, y/d, z/d);
    }
    public float dot(float3 other) {
        return x*other.x + y*other.y + z*other.z;
    }
    public float3 plus(float3 other) {
        return new float3(x+other.x, y+other.y, z+other.z);
    }
    public float3 mult(float c) {
        return new float3(x*c, y*c, z*c);
    }
}