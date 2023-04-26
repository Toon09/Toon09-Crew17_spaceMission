public class LeapFrog3D {
    public static void main(String[] args) {
        Vector3D position = new Vector3D(1, 0, 0); // initial position
        Vector3D velocity = new Vector3D(0, 1, 0); // initial velocity
        double dt = 0.1; // time step
        double t = 0; // initial time
        Vector3D vHalf = velocity.subtract(acceleration(position).scale(0.5 * dt)); // initial half-step velocity

        for (int i = 0; i < 100; i++) {
            position = position.add(vHalf.scale(dt));
            vHalf = vHalf.add(acceleration(position).scale(dt));
            velocity = vHalf.add(acceleration(position).scale(0.5 * dt));
            t += dt;
            System.out.println(t + " " + position + " " + velocity);
        }
    }

    public static Vector3D acceleration(Vector3D position) {
        return new Vector3D(-position.x, -position.y, -position.z); // example acceleration function
    }
}

class Vector3D {
    public double x;
    public double y;
    public double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D add(Vector3D other) {
        return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3D subtract(Vector3D other) {
        return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3D scale(double scalar) {
        return new Vector3D(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
