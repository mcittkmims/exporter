import os
import json

def count_json_objects_in_folder(folder_path):
    total_count = 0
    file_counts = {}

    for filename in os.listdir(folder_path):
        if filename.endswith('.json'):
            file_path = os.path.join(folder_path, filename)
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                    if isinstance(data, list):
                        count = len(data)
                        total_count += count
                        file_counts[filename] = count
                    else:
                        print(f"Warning: {filename} does not contain a JSON list, skipping.")
            except Exception as e:
                print(f"Error reading {filename}: {e}")

    return total_count, file_counts

if __name__ == "__main__":
    folder_path = '/Users/mcittkmims/Downloads/Hojinjoho_20250523'
    total_count, file_counts = count_json_objects_in_folder(folder_path)

    print("\nCount per file:")
    for filename, count in file_counts.items():
        print(f"{filename}: {count}")

    print(f"\nTotal JSON objects found across all files: {total_count}")
