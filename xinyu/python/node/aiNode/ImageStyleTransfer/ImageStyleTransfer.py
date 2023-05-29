# Created by Xinyu Zhu on 1/3/2023, 9:40 PM
from node.aiNode.ImageStyleTransfer.evaluate import process_image
from common.io.file import project_input_root
from common.io.file.BatchFileClient import get_all_filepath
import os

model_root = os.path.join(project_input_root, "fast_style_transfer_models")

avilable_models = get_all_filepath(model_root)
model_store = {}
for model in avilable_models:
    model_store[os.path.basename(model).replace(".ckpt", "")] = model

if __name__ == '__main__':
    for model in model_store:
        process_image("D:/Work/3DWorkspace/MMDResource/human/精选/R18/YYB_Dracula/Dracula_ycyxzPreview.png",
                      "D:/Work/3DWorkspace/MMDResource/human/精选/R18/YYB_Dracula/Dracula_{model_name}.png".format(
                          model_name=model),
                      model_store[model])
