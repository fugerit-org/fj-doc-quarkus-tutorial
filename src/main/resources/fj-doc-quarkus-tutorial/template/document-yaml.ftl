<#assign defaultTitle="My sample title Source YAML">
---
xmlns: "http://javacoredoc.fugerit.org"
xsi:schemaLocation: "http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-1.xsd"
xmlns:xsi: "http://www.w3.org/2001/XMLSchema-instance"
_t: "doc"
_e:
- _t: "metadata"
  _e:
  - name: "margins"
    _t: "info"
    _v: "10;10;10;30"
  - name: "doc-title"
    _t: "info"
    _v: "${docTitle!defaultTitle}"
  - name: "doc-author"
    _t: "info"
    _v: "fugerit79"
  - name: "doc-language"
    _t: "info"
    _v: "en"
  - name: "excel-table-id"
    _t: "info"
    _v: "data-table=print"
  - name: "csv-table-id"
    _t: "info"
    _v: "data-table"
  - name: "default-font-name"
    _t: "info"
    _v: "TitilliumWeb"
  - _t: "footer-ext"
    _e:
    - align: "right"
      _t: "para"
      _v: "${r"${currentPage}"} / ${r"${pageCount}"}"
- _t: "body"
  _e:
  - head-level: "1"
    _t: "h"
    _v: "${docTitle!defaultTitle}"
  - padding: "2"
    columns: "3"
    width: "100"
    id: "data-table"
    colwidths: "30;30;40"
    _t: "table"
    _e:
    - header: "true"
      _t: "row"
      _e:
      - align: "center"
        _t: "cell"
        _e:
        - _t: "para"
          _v: "Name"
      - align: "center"
        _t: "cell"
        _e:
        - _t: "para"
          _v: "Surname"
      - align: "center"
        _t: "cell"
        _e:
        - _t: "para"
          _v: "Title"
        <#if listPeople??>
            <#list listPeople as current>
    - _t: "row"
      _e:
      - _t: "cell"
        _e:
        - _t: "para"
          _v: "${current.name}"
      - _t: "cell"
        _e:
        - _t: "para"
          _v: "${current.surname}"
      - _t: "cell"
        _e:
        - _t: "para"
          _v: "${current.title}"
            </#list>
        </#if>