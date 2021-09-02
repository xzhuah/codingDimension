# Created by Xinyu Zhu on 2021/9/1, 22:09
import numpy as np


# exclusive tags optimal grouping algorithm
# Given the exclusive relationship between tags
# {tag1, tag2}, {tag2, tag3}, {tag4, tag5, tag6}
# Find a way to group these tags such that, tags in the same group are not exclusive, and the number of total
# group is minimized.

def optimal_exclusive_grouping(exclusive_sets: list) -> list:
    # create a dictionary for fast fetching exclusive elements for each elements
    exclusive_map = dict()
    for exclusive_set in exclusive_sets:
        for tag in exclusive_set:
            if tag not in exclusive_map:
                exclusive_map[tag] = set()

    # O(n^2)
    for exclusive_set in exclusive_sets:
        for tag1 in exclusive_set:
            for tag2 in exclusive_set:
                exclusive_map[tag1].add(tag2)

    all_result = []
    all_tags = list(exclusive_map.keys())

    optimal_helper([], all_tags, 0, exclusive_map, all_result)

    optimal_result_index = -1
    optimal_result_length = -1
    group_length = []
    for i, result in enumerate(all_result):
        length = []
        for group in result:
            length.append(len(group))
        group_length.append(length)

        if optimal_result_length == -1:
            optimal_result_index = i
            optimal_result_length = len(result)
        elif len(result) < optimal_result_length:
            optimal_result_length = len(result)
            optimal_result_index = i

    # we also want the length variance be as small as possible
    # so that the size of each group can be as close as possible

    optimal_var_index = -1
    optimal_var = -1
    for i, result in enumerate(all_result):
        if len(result) > optimal_result_length:
            continue
        current_var = np.var(group_length[i])
        if optimal_var == -1:
            optimal_var = current_var
            optimal_var_index = i
        elif current_var < optimal_var:
            optimal_var = current_var
            optimal_var_index = i
    return all_result[optimal_var_index]


def optimal_helper(existing_groups, all_tags, next_pos, exclusive_map, result_holder=[]):
    if next_pos >= len(all_tags):
        result_holder.append(existing_groups)
        return

    next_tag = all_tags[next_pos]

    for i, group in enumerate(existing_groups):
        exclusive = False
        for tag in group:
            if tag in exclusive_map[next_tag]:
                exclusive = True
                break
        if not exclusive:
            existing_groups_copy = copy_2d_list(existing_groups)
            existing_groups_copy[i].append(next_tag)
            optimal_helper(existing_groups_copy, all_tags, next_pos + 1, exclusive_map, result_holder)

    existing_groups_copy = copy_2d_list(existing_groups)
    existing_groups_copy.append([next_tag])
    optimal_helper(existing_groups_copy, all_tags, next_pos + 1, exclusive_map, result_holder)


def copy_2d_list(existing_groups):
    existing_groups_copy = []
    for group in existing_groups:
        new_list = []
        for tag in group:
            new_list.append(tag)
        existing_groups_copy.append(new_list)
    return existing_groups_copy


if __name__ == '__main__':
    exclusive_sets = [{'a', 'b', 'c'}, {'d', 'e'}, {'f', 'g', 'h', 'i'}, {'a', 'b', 'd'}, {'c'}, {'e', 'f', 'h'},
                      {'g', 'i'}]

    result = optimal_exclusive_grouping(exclusive_sets)
    print(result)
