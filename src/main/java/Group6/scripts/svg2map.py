#!/usr/bin/env python3
# Super shitty script to partially transform an SVG file into a Proj-2-1 map

import argparse
import svgpathtools
import os


# Inkscape for some reason writes lines like: d="m 59.999998,41 v 46 h 1.000001 V 41 Z"
# For the sake of clarity we round up/down (hopefully this doesn't break anything..)
def sanitize_point(point: complex) -> (float, float):
    return round(point.real, 4), round(point.imag, 4)


# An area/wall is a closed path with 4 points, no crossing lines
# returns string: x1,y1,x2,y2,x3,y3,x4,y4
def get_csv_coor(path):
    points = []
    for line in path:
        x, y = sanitize_point(line.point(0))
        points.append(x)
        points.append(y)

    return ','.join((str(c) for c in points))


description = '''
This simple script transforms simple .dxf files into map files for DKE-Project-2-2.
'''

usage = '''
e.g:
{}  ../resources/maps/map_simple_test/drawing.svg ../resources/maps/map_simple_test/drawing.p22map
'''.format(os.sys.argv[0])


parser = argparse.ArgumentParser(description=description, usage=usage)
parser.add_argument("input", help="input file (.svg)")
parser.add_argument("output", help="output file (.p22map)")

argsp = parser.parse_args()

input_path = argsp.input
output_path = argsp.output
output_lines = []

# Quick sanity check
if input_path == output_path:
    print("Output file and input file are the same!")
    exit(1)

paths, attributes = svgpathtools.svg2paths(input_path)

for a, p in zip(attributes, paths):

    if a['id'] == 'targetArea':
        output_lines.append('targetArea = ' + get_csv_coor(p) + "\n")
    elif a['id'] == 'spawnAreaIntruders':
        output_lines.append('spawnAreaIntruders = ' + get_csv_coor(p) + "\n")
    elif a['id'] == 'spawnAreaGuards':
        output_lines.append('spawnAreaGuards = ' + get_csv_coor(p) + "\n")
    elif 'window' in a['id']:
        output_lines.append('window = ' + get_csv_coor(p) + "\n")
    elif 'door' in a['id']:
        output_lines.append('door = ' + get_csv_coor(p) + "\n")
    elif 'shaded' in a['id']:
        output_lines.append('shaded = ' + get_csv_coor(p) + "\n")
    elif 'teleport' in a['id']:
        output_lines.append(a['id'] + ' = ' + get_csv_coor(p) + "\n")
    else:
        output_lines.append('wall = ' + get_csv_coor(p) + "\n")

with open(output_path, 'w') as output_file:
    output_file.writelines(output_lines)


