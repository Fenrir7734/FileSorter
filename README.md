# FileSorter
This is GUI program that allows the user to select multiple files and directories and sort them according to the given rules. By default, application will sort (and if you wish, rename) all files from specified directories and all of their subdirectories, but you can define filter rules for omitting certain files. After sorting is complete, the program can remove all empty directories. Program does not allow sorting in place, you must create empty target directory to which sorted files will be moved or copied.  
**Program was made to be used on Unix-like systems, Windows is not supported.**

## Features
- Sorting files
- Renaming files
- Filtering files
- Sorting from multiple directories at once
- Defining rule groups and saving them for later use
- Coping or moving sorted files to target directory
- Removing empty directories
- Backups

## Rules syntax
Program uses simple language to define rules. You can define rules by directly writing expressions in this language or by using Rule Builder.  
Language consists of three types of operators - `provider`, `predicate` and `action`.

### Provider
Providers are used to define sort and rename rules. Their syntax is as easy as `%(TOKEN:args)` where `TOKEN` should be replaced by token of specific provider and `args` by an argument. Providers are used to define names (or their parts) of files, directories or as argument for `predicate`, for example: provider `%(EXT)` will extract extension from file and if used in sort or rename this extracted extension will be placed in directory or file name. You can use multiple providers at once for building file path or file name by simply putting them one after another.

| Token | Name                     |             Arguments             | Return type  | Sort | Rename | Filter |
|:-----:|--------------------------|:---------------------------------:|--------------|:----:|:------:|:------:|
| `FIN` | file name                |                 0                 | String       |  ❌   |   ✅    |   ✅    |
| `FIX` | file name with extension |                 0                 | String       |  ❌   |   ✅    |   ✅    |
| `EXT` | file extension           |                 0                 | String       |  ✅   |   ✅    |   ✅    |
| `CAT` | file category            |                 0                 | Exact String |  ✅   |   ✅    |   ✅    |
| `DIN` | directory name           |                 0                 | String       |  ✅   |   ✅    |   ✅    | 
| `FIP` | file path                |                 0                 | Path         |  ❌   |   ❌    |   ✅    |
| `DIP` | directory path           |                 0                 | Path         |  ❌   |   ❌    |   ✅    |
| `FIS` | file size                |                 0                 | Number       |  ❌   |   ❌    |   ✅    |
| `DIM` | image dimensions         |                 0                 | Number       |  ✅   |   ✅    |   ✅    |
| `WID` | image width              |                 0                 | Number       |  ✅   |   ✅    |   ✅    |
| `HEI` | image height             |                 0                 | Number       |  ✅   |   ✅    |   ✅    |
| `DAC` | date created             | 1 - [Date pattern](#Date-pattern) | Date         |  ✅   |   ✅    |   ✅    |
| `DAM` | date modified            | 1 - [Date pattern](#Date-pattern) | Date         |  ✅   |   ✅    |   ✅    |
| `DAA` | date accessed            | 1 - [Date pattern](#Date-pattern) | Date         |  ✅   |   ✅    |   ✅    |
| `DAU` | current date             | 1 - [Date pattern](#Date-pattern) | Date         |  ✅   |   ✅    |   ❌    |
| `TXT` | custom text              |           1 - any text            | String       |  ✅   |   ✅    |   ❌    |
|  `/`  | file separator           |                 0                 | None         |  ✅   |   ❌    |   ❌    |

#### Date pattern
[Here](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html) at *Patterns for Formatting and Parsing* section you can read about date patterns.

### Predicate
Predicates, combines with providers and action are used in filter rules. Their syntax is as follows: `%(TOKEN:args...)` where, `TOKEN` should be replaced by token of specific predicate and `args...` by list of arguments, for example: `%(==:1920x1080,2560x1080)` will compare some value with `1920x1080` and `2560x1080`. If any comparison returns true then entire predicate will be true. 

| Token | Name             | Arguments | String | Exact String | Path | Number | Date | 
|:-----:|------------------|:---------:|:------:|:------------:|:----:|:------:|:----:|
| `==`  | equal            |    1..    |   ✅    |      ✅       |  ✅   |   ✅    |  ✅   |
| `!=`  | not equal        |    1..    |   ✅    |      ✅       |  ✅   |   ✅    |  ✅   |
|  `>`  | greater than     |     1     |   ❌    |      ❌       |  ❌   |   ✅    |  ✅   |
| `>=`  | greater or equal |     1     |   ❌    |      ❌       |  ❌   |   ✅    |  ✅   |
|  `<`  | smaller than     |     1     |   ❌    |      ❌       |  ❌   |   ✅    |  ✅   |
| `<=`  | smaller or equal |     1     |   ❌    |      ❌       |  ❌   |   ✅    |  ✅   |
| `CON` | contains         |    1..    |   ✅    |      ❌       |  ❌   |   ❌    |  ❌   |
| `NCO` | not contains     |    1..    |   ✅    |      ❌       |  ❌   |   ❌    |  ❌   |
| `SW`  | starts with      |    1..    |   ✅    |      ❌       |  ❌   |   ❌    |  ❌   |
| `EW`  | ends with        |    1..    |   ✅    |      ❌       |  ❌   |   ❌    |  ❌   |

### Action
Actions are used only in filter rules, always at the beginning of expression. Purpose of action is to define what should be done if predicate returns true. 

| Token | Name    |
|:-----:|---------|
| `INC` | Include |
| `EXC` | Exclude |

## Rules

### Sort and rename rules
Sort and rename rules are defined using only providers. If you define expression consisting of multiple providers, output of these rules will be merged together to single string (rename rule) or to single path (sort rule).

#### Rename rule example
```
%(FIN)%(TXT:-)%(CAT)%(TXT:.)%(EXT)
```
Execution of this rule will produce file names according to pattern `old_file_name-file_category.file_extension`, for example:
```
my_photo-image.jpg
selfie-image.png
my_document-text.doc
some music-audio.mp3
```

#### Sort rule example
```
%(CAT)%(/)%(EXT)
```
Execution of this rule will produce file path according to pattern `file_category/file_extension`, so directory structure may look something like this:
```
target_dir/
├── image/
|   ├── jpg/
|   |   ├── photo1.jpg
|   |   └── photo2.jpg
|   └── png/
|       ├── photo3.png
|       └── photo4.png
└── text
    ├── doc/
    |   └── document1.doc
    └── txt/ 
        ├── notes3.txt
        └── notes2.txt   
```

### Filter rule
Filter rule always consists of three elements: action, provider and predicate in that exact order.
```
%(EXC)%(DIM)%(==:1920x1080,2560x1080)
```
This expression excludes from sorting all images with dimensions equal to 1920x1080 or 2560x1080.
