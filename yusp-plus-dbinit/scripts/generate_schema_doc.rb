#!/usr/bin/env ruby
# frozen_string_literal: true

# Generates the AI-coding database reference from the authoritative GoldenDB
# initialization script. Keep this mechanical so the reference can be refreshed
# whenever the DDL changes.

source = ARGV.fetch(0, "yusp-plus-dbinit/src/main/resources/sql/goldendb/oca-init-20250915.sql")
target = ARGV.fetch(1, "docs/ai-coding/database-schema.md")

sql = File.read(source, encoding: "UTF-8")
tables = []
expected_table_count = sql.scan(/CREATE\s+TABLE\s+`[^`]+`/i).length

sql.scan(/CREATE\s+TABLE\s+`([^`]+)`\s*\((.*?)\)\s*(ENGINE\s*=?\s*.*?;)/mi) do |name, body, table_options|
  columns = []
  constraints = []
  table_comment = table_options[/\bCOMMENT\s*=\s*'([^']*)'/i, 1] || ""

  body.each_line do |line|
    item = line.strip.sub(/,\s*\z/, "")
    next if item.empty?

    if (match = item.match(/\A`([^`]+)`\s+(.+)\z/m))
      column_name = match[1]
      definition = match[2]
      comment = definition[/\bCOMMENT\s+'((?:''|[^'])*)'/i, 1]&.gsub("''", "'") || ""
      type_end = definition.index(/\s+(?:CHARACTER\s+SET|COLLATE|NOT\s+NULL|NULL\b|DEFAULT\b|AUTO_INCREMENT\b|COMMENT\b|PRIMARY\s+KEY\b|UNIQUE\b)/i)
      type = (type_end ? definition[0...type_end] : definition).strip
      nullable = definition.match?(/\bNOT\s+NULL\b/i) ? "NO" : "YES"
      default_match = definition.match(/\bDEFAULT\s+('(?:''|[^'])*'|[^\s,]+)/i)
      default_value = default_match ? default_match[1] : "—"
      extras = []
      extras << "AUTO_INCREMENT" if definition.match?(/\bAUTO_INCREMENT\b/i)
      extras << "inline PRIMARY KEY" if definition.match?(/\bPRIMARY\s+KEY\b/i)
      extras << "inline UNIQUE" if definition.match?(/\bUNIQUE\b/i)
      columns << [column_name, type, nullable, default_value, extras.join(", "), comment]
    elsif item.match?(/\A(?:PRIMARY\s+KEY|UNIQUE\s+(?:KEY|INDEX)|KEY|INDEX|CONSTRAINT|FOREIGN\s+KEY)/i)
      constraints << item.gsub(/\s+/, " ")
    end
  end

  tables << [name, table_comment, columns, constraints]
end

if tables.length != expected_table_count
  abort "Parsed #{tables.length} of #{expected_table_count} CREATE TABLE statements from #{source}"
end
abort "A parsed table has no columns" if tables.any? { |_name, _comment, columns, _constraints| columns.empty? }

File.open(target, "w:UTF-8") do |file|
  file.puts "# GoldenDB 数据模型（JDK 17 / master）"
  file.puts
  file.puts "> 自动生成自 `#{source}`。共 #{tables.length} 张表。字段名、类型、空值、默认值、键与外键均以原始 DDL 为准；不要手工修改本文件。"
  file.puts "> 重新生成：`ruby yusp-plus-dbinit/scripts/generate_schema_doc.rb`。"
  file.puts
  file.puts "## 表目录"
  file.puts
  tables.each do |name, table_comment, _columns, _constraints|
    anchor = name.downcase.gsub(/[^a-z0-9_-]/, "")
    label = table_comment.empty? ? name : "#{name} — #{table_comment}"
    file.puts "- [#{label}](##{anchor})"
  end

  tables.each do |name, table_comment, columns, constraints|
    file.puts
    file.puts "## #{name}"
    file.puts
    file.puts(table_comment.empty? ? "（DDL 未提供表注释）" : table_comment)
    file.puts
    file.puts "| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |"
    file.puts "|---|---|:---:|---|---|---|"
    columns.each do |column|
      values = column.map { |value| value.to_s.gsub("|", "\\|").gsub("\n", " ") }
      file.puts "| `#{values[0]}` | `#{values[1]}` | #{values[2]} | `#{values[3]}` | #{values[4].empty? ? '—' : values[4]} | #{values[5].empty? ? '—' : values[5]} |"
    end
    file.puts
    file.puts "表级约束/索引："
    if constraints.empty?
      file.puts "- 无显式表级约束或索引。"
    else
      constraints.each { |constraint| file.puts "- `#{constraint.delete('`')}`" }
    end
  end
end

warn "Generated #{target} with #{tables.length} tables"
