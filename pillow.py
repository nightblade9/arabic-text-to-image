# https://stackoverflow.com/questions/18942605/how-to-use-unicode-characters-with-pil
#!/usr/bin/python
# -*- coding: utf-8 -*-
from PIL import Image, ImageDraw, ImageFont, ImageFilter

#configuration
font_size=36
width=200
height=100
back_ground_color=(255,255,255)
font_size=36
font_color=(0,0,0)
unicode_text = u"بِسمللَه"

im  =  Image.new ( "RGB", (width,height), back_ground_color )
draw  =  ImageDraw.Draw ( im )
unicode_font = ImageFont.truetype("A_Nefel_Sereke.ttf", font_size)
draw.text ( (10,10), unicode_text, font=unicode_font, fill=font_color )

im.save("text.png")