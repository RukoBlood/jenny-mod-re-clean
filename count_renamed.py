import os
import re
from pathlib import Path
import javalang

# Папка с исходным кодом Java (укажите ваш путь)
SRC_DIR = "./src"
OUTPUT_MD = "renamed_stats.md"

# Исключения из синтетических имён (осмысленные короткие имена/сокращения)
EXCLUDED_NAMES = {
    "mc", "id", "x", "y", "z", "mu"
}

# Регулярные выражения для одиночных обфусцированных имён
SIMPLE_OBF_PATTERNS = [
    r"^[a-zA-Z]{1,2}$",              # 1-2 буквы (a, l, g, e, aY)
    r"^[a-zA-Z]{1,2}_\d*$",          # Буквы с цифрами через _ (a_9, b_12)
    r"^[a-zA-Z]+_$",                 # Имя, заканчивающееся на _ (d_)
    r"^_+$",                         # Только подчеркивания
    r"^(void|int|boolean|long|byte|short|char|float|double)_[a-zA-Z0-9]+$", # Имена типа void_e, int_a
]

# Известные префиксы деобфусцированных пакетов
PACKAGE_PREFIXES = ("java_lang_", "net_minecraft_", "org_lwjgl_", "com_", "javax_")

SIMPLE_OBF_REGEX = re.compile("|".join(SIMPLE_OBF_PATTERNS))


def is_synthetic(raw_name: str) -> bool:
    """Проверяет, является ли имя поля/метода синтетическим (обфусцированным)."""

    # 1. Очистка имени от возможного захвата скобок/аргументов
    name = re.sub(r"\(.*?\)", "", raw_name).strip()

    if not name:
        return False

    # 2. Белый список исключений
    if name in EXCLUDED_NAMES:
        return False

    # 3. Игнорируем константы в UPPER_SNAKE_CASE (например: STRIP_BUTTON_ID, MOD_ID, DEG_90)
    # Если имя состоит только из ЗАГЛАВНЫХ букв, цифр и _ — это осмысленная константа.
    if name.isupper() and not name.startswith("VOID_"):
        return False

    # 4. Проверка на простые синтетические имена (a, void_e, d_, a_9)
    if SIMPLE_OBF_REGEX.match(name):
        return True

    # 5. Проверка на деобфусцированные имена с пакетами (java_lang_String_a, net_minecraft_util_math_Vec3d_B)
    parts = name.split('_')
    if len(parts) >= 3:
        last_part = parts[-1]
        # Последняя часть — это 1-2 обфусцированные буквы (например, '_a', '_B', '_9')
        if len(last_part) <= 2:
            if name.startswith(PACKAGE_PREFIXES):
                return True

            # Проверяем, что первая часть содержит lowercase (это пакет/класс, а не константа)
            prefix_without_last = "_".join(parts[:-1])
            if not prefix_without_last.isupper():
                return True

    return False


def analyze_java_file(file_path: Path):
    total_methods = 0
    synthetic_methods = 0
    total_fields = 0
    synthetic_fields = 0

    found_synthetics = []

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
    print("\n" + "=" * 40)
    print(f"Итоги анализа:")
    print(f"Методы переименованы: {pct_m:.2f}% ({renamed_m}/{total_m})")
    print(f"Поля переименованы: {pct_f:.2f}% ({renamed_f}/{total_f})")
    print("=" * 40)

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
        clean_file = item['file'].replace('|', '\\|')
        clean_name = item['name'].replace('|', '\\|')
        md_lines.append(f"| `{clean_file}` | {item['type']} | `{clean_name}` |")

    with open(OUTPUT_MD, "w", encoding="utf-8") as f:
        f.write("\n".join(md_lines) + "\n")

    print(f"\n[+] Отчет сохранен в файл: {OUTPUT_MD}")


if __name__ == "__main__":
    main()