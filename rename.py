import os

def rename_files(string, old_term, new_term):
    os.chdir(string)
    file_list = os.listdir(string)
    print(file_list)

    for old_name in file_list:

        old_term = old_term.lower()
        new_term = new_term.lower()
        term_search = old_name.find(old_term)
        if (term_search != -1):
            new_name = old_name.replace(old_term, new_term)
            os.rename(old_name, new_name)
        
        old_term = old_term.upper()
        new_term = new_term.upper()
        term_search = old_name.find(old_term)
        if (term_search != -1):
            new_name = old_name.replace(old_term, new_term)
            os.rename(old_name, new_name)
        
    file_list = os.listdir(string)
    print(file_list)

old_term = "electric"
new_term = "plasma"

rename_files(r"C:\users\admin\desktop\GigaGal\core\rawAssets\sprites", old_term, new_term)
rename_files(r"C:\users\admin\desktop\GigaGal\GigaGalLevels\scenes", old_term, new_term)
rename_files(r"C:\users\admin\desktop\GigaGal\GigaGalLevels\assets\orig\images", old_term, new_term)
