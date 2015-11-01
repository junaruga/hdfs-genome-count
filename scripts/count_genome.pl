#!/usr/bin/env perl

use strict;
use warnings;
use Carp;
use English qw( -no_match_vars );
use Try::Tiny;

my $STATUS = +{
    ok    => 0,
    error => 1,
};

my $GENOME_FILE = '/tmp/genome.txt';
my $_queues_ref = [];

sub main {
    my $exit_status = $STATUS->{ok};
    try {
        my $fh = undef;
        try {
            open $fh, '<', $GENOME_FILE
                or croak "$GENOME_FILE $OS_ERROR";

            # Implement as a map-reduce style intentionally.
            while (my $line = <$fh>) {
                chomp $line;
                my ($line, $counted_map) = map_for_line($line);
                push @{$_queues_ref}, +{
                    key => $line,
                    value => $counted_map,
                };
            }

            my $total_map = +{};
            while (scalar @{$_queues_ref} > 0) {
                my $queue = shift @{$_queues_ref};
                $total_map = reduce($total_map, $queue->{value});
            }

            print_counted_map($total_map);
        }
        finally {
            if ( defined $fh ) {
                close $fh;
            }
        };
    }
    catch {
        $exit_status = $STATUS->{error};
    };

    return $exit_status;
}

sub map_for_line {
    my ($line) = @_;

    my $counted_map = +{};

    if ( !defined $line ) {
        croak 'line is required.';
    }
    for (my $pos = 0; $pos < length($line); $pos++) {
        my $c = substr($line, $pos, 1);
        $counted_map->{$c}++;
    }

    # map's key, value
    return ($line, $counted_map);
}

sub reduce {
    my ($total_map, $map) = @_;

    if ( !defined $total_map || !defined $map ) {
        croak 'total_map or map is required.';
    }

    for my $c (keys %{$map}) {
        $total_map->{$c} += $map->{$c};
    }

    return $total_map;
}

sub print_counted_map {
    my ($counted_map) = @_;

    my $total = 0;
    for my $c (sort(keys %{$counted_map})) {
        printf "%s: %d\n", $c, $counted_map->{$c};
        $total += $counted_map->{$c};
    }
    printf "Total: %d\n", $total;

    return;
}

my $exit_status = main();
exit $exit_status;

