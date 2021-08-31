# Created by Xinyu Zhu on 2021/8/31, 2:11
from common.io.file.PlainTextClient import read_file, get_file
from node.tagTreeNote.parsers.Parsers import MarkdownParser
from node.tagTreeNote.renders.Renders import MarkdownRender
from node.tagTreeNote.models.File import FileCollection


# maintain common work flows
def markdown_to_markdown(source_markdown: str, result_markdown: str, processor_func=lambda x: x):
    content = read_file(source_markdown)
    markdown_parser = MarkdownParser()
    file_collection = markdown_parser.parse_file_collection_without_metadata_or_tags(content)
    print(file_collection)

    # Do what you want
    processed_collection = processor_func(file_collection)

    markdown_render = MarkdownRender()
    with get_file(result_markdown, "w") as f:
        f.write(markdown_render.render_file_collection(processed_collection))


if __name__ == '__main__':
    def processor(file_col: FileCollection) -> FileCollection:
        return file_col.filter_by_path_("b/b/e").filter_by_tags_or({"middle", "3"})
    markdown_to_markdown("file_index.md", "file_index.md", processor)
