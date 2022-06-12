import os
import shutil


def main():
    source_dir = os.path.join('.', 'src')
    output_dir = os.path.join('.', 'target')
    resources_path = os.path.join(output_dir, 'main', 'resources')
    config_filename = 'config.json'
    current_config_path = os.path.join('.', config_filename)
    destination_path = os.path.join(resources_path, config_filename)

    java_files = [os.path.join(root, file) for root, _, files in os.walk(source_dir) for file in files if file.endswith('.java')]
    os.system(f'javac -d {output_dir} {" ".join(java_files)}')

    if not os.path.exists(resources_path):
        os.mkdir(resources_path)

    shutil.copyfile(current_config_path, destination_path)
    os.chdir(output_dir)
    os.system('java main.java.main.Main')


if __name__ == '__main__':
    main()
