#!/usr/bin/python3

"""Generate a class containing all resources from a given directory.

Usage:
    makeresfile [-v --verbose] <directory> [--ext=<ext>] [-o --output=<output>]
    makeresfile -h | --help
    makeresfile --version

Arguments:
    <directory>             Directory containing your resources.

Options:
    --version               Show the version number.
    -h --help               Show these information.
    -v --verbose            Furnish more information of the process.
    --ext=<ext>             Specify which type of file include to
                            resources file [default: png].
    -o --output=<output>    Specify output location [default: .].

"""

import os
import docopt
import fnmatch


def generate_res_file(args):
    """Stocks image's paths to use them easily."""
    verbose = False
    if args['--verbose']:
        verbose = True

    ext = args['--ext']

    # Define the path where the class will be generate
    class_name = os.path.basename(args['<directory>'])
    output_file = os.path.join(args['--output'], class_name + ".py")

    # Get all images from the images directory
    resources = []
    for root, dirnames, filenames in os.walk(args['<directory>']):
        for filename in fnmatch.filter(filenames, '*.{}'.format(ext)):
            resources.append(os.path.join(root, filename))

    # Set an attribute for each file found
    class_attributes = []
    for res in resources:
        # Define the attribute name of the future class
        attr_name = os.path.splitext(os.path.basename(res))[0]
        if verbose:
            print("The file: {} has been added as {}".format(
                res, attr_name)
            )
        class_attributes += [(attr_name, res)]

    # Generate the class file
    with open(output_file, 'w') as class_file:
        class_file.write("class {}Res:\n".format(class_name.title()))
        for attr in class_attributes:
            class_file.write("    {} = '{}'\n".format(*attr))

        # Write the getter function
        getter = """\n    @classmethod\n    def get(cls, string):
        return cls.__getattribute__(cls, string)"""
        class_file.write(getter)


def main():
    """Launch the script."""
    # Parsing given arguments
    args = docopt.docopt(__doc__, version='makeresfile 0.0.1')

    if args['<directory>']:
        if os.path.isdir(args['<directory>']):
            generate_res_file(args)

if __name__ == '__main__':
    main()
