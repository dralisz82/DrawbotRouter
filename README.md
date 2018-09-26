# DrawbotRouter
DrawbotRouter generates Arduino C code for a 2 wheeled differential drive drawing robot from vector graphics in SVG format.

Supported robot: http://hungarianrobot.hu/wordpress/portfolio-item/drawbot/

Supported SVG primitives:
Currently only path tags containing straight sections are supported.
Example: <path d="M 0 0 L 4 0 V 6 h -4 z" />

Compatible SVG sources:
TinkerCAD provides compatible SVG export (https://www.tinkercad.com/)
Inkscape provides compatible SVG files if below instructions are followed:
   * Path menu -> Object to Path
   * Path menu -> Path Effects...
   * On Path Effects pane Add path effects (click green "+")
   * Pick Interpolate points
   * Set Interpolator type to Linear

Source code:
Source is shared as an Eclipse project

Usage:
cd <project folder>/bin
java greszler.szilard.hurba.drawbotrouter.DrawBotRouter input.svg >target.ino
