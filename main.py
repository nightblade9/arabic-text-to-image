# https://stackoverflow.com/questions/17856242/convert-string-to-image-in-python
from PIL import Image
from PIL import ImageDraw
from PIL import ImageFont

OUTLINE_THICKNESS = 3

def get_size(text, font):
  img = Image.new('RGB', (1, 1))
  d = ImageDraw.Draw(img)
  d.text((0, 0), text, fill=(255, 0, 0))
  text_width, text_height = d.textsize(text)
  print("Text size: {}".format((text_width, text_height)))
  return (text_width, text_height)

def generate_image(text, font_name="arial.ttf", font_size=12, text_colour=(255, 255, 255)):
  font_size = 11   

  outline_colour = "black"
  colorBackground = "white"

  font = ImageFont.truetype(font_name, font_size)
  image_width, image_height = get_size(text, font)
  img = Image.new('RGB', (image_width, image_height * 2), text_colour)
  d = ImageDraw.Draw(img)
  d.text((2, image_height / 2), text, fill=(0, 0, 0), font=font)

  img.save("image.png")
  print("Generated image.png")

if __name__ == "__main__":
    generate_image("Hello, world.")