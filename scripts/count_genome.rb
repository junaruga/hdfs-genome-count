#!/usr/bin/env ruby

GENOME_FILE = '/tmp/genome.txt'

STATUS = {
  ok: 0,
  error: 1,
}
$queues = []

def main()
  exit_status = STATUS[:ok]

  begin
    open(GENOME_FILE) { |file|
      # Implement as a map-reduce style intentionally.
      while line = file.gets
        line.chomp!
        map_obj = map_for_line(line)

        queue = {
          key: line,
          value: map_obj[:value],
        }
        $queues.push(queue)
      end

      total_map = {}
      while $queues.size > 0
        queue = $queues.shift
        total_map = reduce!(total_map, queue[:value])
      end

      print_counted_map(total_map)
    }
  rescue RuntimeError => e
    puts e
    exit_status = STATUS[:error]
  end
end

def map_for_line(line)
  counted_map = {}

  if line.nil?
    raise ArgumentError.new("line is required.")
  end
  line.each_char { |c|
    if !counted_map.key?(c)
      counted_map[c] = 0
    end
    counted_map[c] += 1
  }

  { key: line,
    value: counted_map,
  }
end

def reduce!(total_map, map)
  if total_map.nil? or map.nil?
    raise ArgumentError.new("total_map or map is required.")
  end

  map.each_pair { |c, count|
    if !total_map.key?(c)
        total_map[c] = 0
    end
    total_map[c] += count;
  }

  total_map
end

def print_counted_map(counted_map)
  if counted_map.nil?
    raise ArgumentError.new("counted_map is required.")
  end

  total = 0
  sorted_keys = counted_map.each_key
    .sort
  sorted_keys.each { |c|
    count = counted_map[c]
    puts "#{c}: #{count}\n"
    total += count
  }
  puts "Total: #{total}\n"
end

main

exit(0)
