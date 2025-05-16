import os
import re

def update_java_imports(root_dir):
    """
    Updates Java import statements in all .java files within a directory tree to remove
    the incorrect ".com.bennieslab.docdbsystem" part.

    Args:
        root_dir (str): The root directory to start the search from.
    """
    for dirpath, dirnames, filenames in os.walk(root_dir):
        for filename in filenames:
            if filename.endswith(".java"):
                filepath = os.path.join(dirpath, filename)
                try:
                    with open(filepath, "r", encoding="utf-8") as f:
                        content = f.read()

                    # Use regular expression to remove the incorrect part
                    pattern = r"\.com\.bennieslab\.docdbsystem"
                    new_content = re.sub(pattern, "", content)

                    if new_content != content:
                        print(f"Updated: {filepath}")
                        with open(filepath, "w", encoding="utf-8") as f:
                            f.write(new_content)
                except Exception as e:
                    print(f"Error processing {filepath}: {e}")

if __name__ == "__main__":
    # Set the root directory where your Java files are located.
    # This should be the base directory of your project, e.g., where your 'com' folder is.
    root_directory = r"C:\\Users\\benso\\Desktop\\Demos\\Web app with database\\project server\\ServerApp\\src\\main\\java"  #  <--- CHANGE THIS TO YOUR ACTUAL ROOT DIRECTORY

    update_java_imports(root_directory)
    print("Import update process completed.")
