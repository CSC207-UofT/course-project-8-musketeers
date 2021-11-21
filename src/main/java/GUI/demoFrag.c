#version 330
out vec4 fragColor;

uniform float xpos;
uniform float ypos;
uniform float vscale;

uniform sampler2D texTest;

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

  vec2 xypos = vec2(xpos, ypos);

  vec2 trans = ((tc*wh - 0.5f) * 1.f + xypos);
  vec2 cpos = trans;

  float x = cpos.x * 8;
  float y = cpos.y * 8;
  //float m = mandel(cx * vscale, cy * vscale);

  // GRAYSCALE
  float m = max(0.f, min(1.f, [INSERT EQUATION HERE]));



  //fragColor = vec4(m, m, m, 1.0);
  fragColor = texture(texTest, tc*wh);
  //fragColor = vec4((tc*wh).x, (tc*wh).y, 0.6, 1.0);
}