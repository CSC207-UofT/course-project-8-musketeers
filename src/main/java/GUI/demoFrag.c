#version 330
out vec4 fragColor;
uniform float xpos;
uniform float ypos;
uniform float vscale;

float mandel (float cx, float cy) {
	float x = 0;
	float y = 0;
	int i;
	for (i = 0; i < 100; i++) {
		if ((x*x + y*y) > 4) {
		  break;
		}
		float xtemp = x*x - y*y + cx;
		y = 2*x*y + cy;
		x = xtemp;
	}
	return sqrt(i / 100.f);
}

void main() {
  vec2 tc = gl_FragCoord.xy;
  vec2 wh = 1 / vec2(800, 800);
  float cx = (((tc*wh).x - 0.5f) * 1.f + xpos);
  float cy = (((tc*wh).y - 0.5f) * 1.f + ypos);
  float m = mandel(cx * vscale, cy * vscale);
  fragColor = vec4(m, m, m, 1.0);
  //fragColor = vec4((tc*wh).x, (tc*wh).y, 0.6, 1.0);
}