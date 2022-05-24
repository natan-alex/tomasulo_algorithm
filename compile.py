import os
import shutil


def main():
    source_dir = os.path.join('.', 'src')
    output_dir = os.path.join('.', 'target')
    config_filename = 'config.json'
    java_files = [os.path.join(root, file) for root, _, files in os.walk(source_dir) for file in files if file.endswith('.java')]
    compile_command = f'javac -d {output_dir} {" ".join(java_files)}'
    os.system(compile_command)
    config_file_path = os.path.join('.', config_filename)
    destination_path = os.path.join(f'{output_dir}', 'main', 'resources', f'{config_filename}')
    shutil.copyfile(config_file_path, destination_path)
    run_command = f'cd {output_dir} && java main.java.main.Main'
    os.system(run_command)


if __name__ == '__main__':
    main()
