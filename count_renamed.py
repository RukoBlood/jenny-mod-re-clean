import os
import re
from pathlib import Path
import javalang

# Папка с исходным кодом Java (укажите ваш путь)
SRC_DIR = "./src"
OUTPUT_MD = "renamed_stats.md"

# Регулярные выражения для определения "синтетических" / обфусцированных имён
# Подходит под: a, ab, a_1, a_b, void_a, d_, _a, aY, l, g, e и т.д.
OBFUSCATED_PATTERNS = [
    r"^[a-zA-Z]{1,2}$",              # 1-2 буквы (a, l, g, e, aY)
    r"^[a-zA-Z]{1,2}_\d*$",          # Буквы с цифрами через _ (a_9, b_12)
    r"^[a-zA-Z]+_$",                 # Имя, заканчивающееся на _ (d_)
    r"^_+$",                         # Только подчеркивания
    r"^(void|int|boolean|long|byte|short|char|float|double)_[a-zA-Z0-9]+$", # Имена типа void_e, int_a
]

# Скомпилированный шаблон для быстрого поиска
OBF_REGEX = re.compile("|".join(OBFUSCATED_PATTERNS))

def is_synthetic(name: str) -> bool:
    """Проверяет, является ли имя поля/метода синтетическим (обфусцированным)."""
    return bool(OBF_REGEX.match(name))

def analyze_java_file(file_path: Path):
    total_methods = 0
    synthetic_methods = 0
    total_fields = 0
    synthetic_fields = 0

    found_synthetics = []  # Список для хранения подробностей по файлу

    try:
        with open(file_path, "r", encoding="utf-8") as f:
            code = f.read()

        tree = javalang.parse.parse(code)
    except Exception as e:
        print(f"[!] Ошибка парсинга {file_path}: {e}")
        return 0, 0, 0, 0, []

    print(f"\n[+] Проверка класса: {file_path}")

    # Сканирование методов
    for _, node in tree.filter(javalang.tree.MethodDeclaration):
        total_methods += 1
        if is_synthetic(node.name):
            synthetic_methods += 1
            print(f"  ├── Синтетический метод: {node.name}()")
            found_synthetics.append({
                "type": "Method",
                "name": f"{node.name}()",
                "file": str(file_path)
            })

    # Сканирование полей
    for _, node in tree.filter(javalang.tree.FieldDeclaration):
        for declarator in node.declarators:
            total_fields += 1
            if is_synthetic(declarator.name):
                synthetic_fields += 1
                print(f"  ├── Синтетическое поле: {declarator.name}")
                found_synthetics.append({
                    "type": "Field",
                    "name": declarator.name,
                    "file": str(file_path)
                })

    return total_methods, synthetic_methods, total_fields, synthetic_fields, found_synthetics

def main():
    src_path = Path(SRC_DIR)
    if not src_path.exists():
        print(f"Ошибка: Директория {SRC_DIR} не найдена.")
        return

    total_m, synthetic_m = 0, 0
    total_f, synthetic_f = 0, 0
    all_synthetics = []

    # Обход всех .java файлов в директории и подпапках
    for root, _, files in os.walk(src_path):
        for file in files:
            if file.endswith(".java"):
                file_path = Path(root) / file
                tm, sm, tf, sf, synthetics = analyze_java_file(file_path)
                total_m += tm
                synthetic_m += sm
                total_f += tf
                synthetic_f += sf
                all_synthetics.extend(synthetics)

    # Расчет переименованных элементов (Всего - Синтетические)
    renamed_m = total_m - synthetic_m
    renamed_f = total_f - synthetic_f

    pct_m = (renamed_m / total_m * 100) if total_m > 0 else 0.0
    pct_f = (renamed_f / total_f * 100) if total_f > 0 else 0.0

    # Вывод в консоль итогов
    print("\n" + "="*40)
    print(f"Итоги анализа:")
    print(f"Методы переименованы: {pct_m:.2f}% ({renamed_m}/{total_m})")
    print(f"Поля переименованы: {pct_f:.2f}% ({renamed_f}/{total_f})")
    print("="*40)

    # Формирование Markdown отчета
    md_lines = [
        f"Methods renamed: {pct_m:.2f}% ({renamed_m}/{total_m})",
        f"Fields renamed: {pct_f:.2f}% ({renamed_f}/{total_f})",
        "",
        "## Synthetic Elements Detailed List",
        "",
        "| File | Type | Name |",
        "| --- | --- | --- |"
    ]

    for item in all_synthetics:
        # Экранируем символы '|', если они случайно попадут в пути
        clean_file = item['file'].replace('|', '\\|')
        clean_name = item['name'].replace('|', '\\|')
        md_lines.append(f"| `{clean_file}` | {item['type']} | `{clean_name}` |")

    with open(OUTPUT_MD, "w", encoding="utf-8") as f:
        f.write("\n".join(md_lines) + "\n")

    print(f"\n[+] Отчет сохранен в файл: {OUTPUT_MD}")

if __name__ == "__main__":
    main()