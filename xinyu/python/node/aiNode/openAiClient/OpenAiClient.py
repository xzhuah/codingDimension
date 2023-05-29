# Created by Xinyu Zhu on 2/6/2023, 12:48 AM
import openai
from common.io.file.ApiKeyClient import get_openai_key
from base64 import b64decode


class OpenAiClient:

    def __init__(self):
        openai.api_key = get_openai_key()

    def text_to_image(self, text, size=1024, number=1, save_path=[]) -> list:
        if isinstance(save_path, str):
            save_path = [save_path]

        size_param = "{s}x{s}".format(s=size)
        number = max(number, len(save_path))
        response = openai.Image.create(
            prompt=text,
            n=number,
            size=size_param,
            response_format="url" if len(save_path) == 0 else "b64_json"
        )
        if save_path is None or len(save_path) == 0:
            result = []
            for data in response['data']:
                result.append(data["url"])
            return result
        else:
            for i, data in enumerate(response['data']):
                filepath = save_path[i]
                if not filepath.endswith(".png"):
                    filepath += ".png"

                with open(filepath, "wb") as f:
                    f.write(b64decode(data["b64_json"]))
            return len(response['data'])

    def extract_keywords(self, text: str):
        response = openai.Completion.create(
            model="text-davinci-003",
            prompt="Extract keywords from this text:\n\n" + text,
            temperature=0.5,
            max_tokens=60,
            top_p=1.0,
            frequency_penalty=0.8,
            presence_penalty=0.0
        )
        response = response["choices"][0]["text"]
        result = response[response.index("Keywords: ") + len("Keywords: "):].split(",")
        return [keywords.strip() for keywords in result]


if __name__ == '__main__':
    client = OpenAiClient()

    print(client.extract_keywords(
        "Black-on-black ware is a 20th- and 21st-century pottery tradition developed by the Puebloan Native American ceramic artists in Northern New Mexico. Traditional reduction-fired blackware has been made for centuries by pueblo artists. Black-on-black ware of the past century is produced with a smooth surface, with the designs applied through selective burnishing or the application of refractory slip. Another style involves carving or incising designs and selectively polishing the raised areas. For generations several families from Kha'po Owingeh and P'ohwhóge Owingeh pueblos have been making black-on-black ware with the techniques passed down from matriarch potters. Artists from other pueblos have also produced black-on-black ware. Several contemporary artists have created works honoring the pottery of their ancestors."))
