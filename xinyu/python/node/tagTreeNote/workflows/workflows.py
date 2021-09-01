# Created by Xinyu Zhu on 2021/8/31, 2:11
from common.io.file.PlainTextClient import read_file, get_file
from node.tagTreeNote.parsers.Parsers import MarkdownParser
from node.tagTreeNote.parsers.DiskFileParser import DiskFileParser
from node.tagTreeNote.renders.Renders import MarkdownRender
from node.tagTreeNote.models.File import FileCollection
import markdown2


# maintain common work flows
def markdown_to_markdown(source_markdown: str, result_markdown: str, processor_func=lambda x: x):
    with get_file(result_markdown, "w") as f:
        f.write(markdown_to_markdown_(source_markdown, processor_func))


def markdown_to_markdown_(source_markdown: str, processor_func=lambda x: x):
    content = read_file(source_markdown)
    markdown_parser = MarkdownParser()
    file_collection = markdown_parser.parse_file_collection(content)

    # Do what you want
    processed_collection = processor_func(file_collection)

    markdown_render = MarkdownRender()
    return markdown_render.render_file_collection(processed_collection)


def disk_to_markdown(root_dir: str, result_markdown: str, processor_func=lambda x: x):
    with get_file(result_markdown, "w") as f:
        f.write(disk_to_markdown_(root_dir, processor_func))


def disk_to_markdown_(root_dir: str, processor_func=lambda x: x):
    parser = DiskFileParser()
    file_collection = parser.parse_file_collection(root_dir)
    # Do what you want
    processed_collection = processor_func(file_collection)
    markdown_render = MarkdownRender()
    return markdown_render.render_file_collection(processed_collection)


def markdown_to_html(markdown_file: str, result_html: str, processor_func=lambda x: x):
    with get_file(result_html, "w") as f:
        f.write(markdown_to_html_(markdown_file, processor_func))


def markdown_to_html_(markdown_file: str, processor_func=lambda x: x):
    content = read_file(markdown_file)
    markdown_parser = MarkdownParser()
    file_collection = markdown_parser.parse_file_collection(content)

    # Do what you want
    processed_collection = processor_func(file_collection)

    markdown_render = MarkdownRender()
    html = markdown2.markdown(markdown_render.render_file_collection(processed_collection))
    return html


if __name__ == '__main__':
    def processor(file_col: FileCollection) -> FileCollection:
        return file_col


    markdown_to_markdown("file_index.md", "file_index.md", processor)

    markdown_to_html("file_index.md", "file_index.html")

    # obtain the structure of this project
    root = "../../../"
    disk_to_markdown(root, "project_structure.md")
