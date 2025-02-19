"use client"

import {
  ColumnDef,
  flexRender,
  getCoreRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  SortingState,
  useReactTable,
} from "@tanstack/react-table"

import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow, } from "@/components/ui/table"

import React, { useEffect, useState } from "react";
import { DataTablePagination } from "@/components/ui/datatable/data-table-pagination";
import Image from "next/image";
import gusSadPinIconSrc from "@/../public/icon/brawler/gus/gus_sad_pin.png";

interface DataTableProps<TData, TValue> {
  columns: ColumnDef<TData, TValue>[];
  data: TData[];
  pagination?: {
    enabled?: boolean;
    size?: number;
  }
}

export function DataTable<TData, TValue>(
  { columns, data, pagination }: DataTableProps<TData, TValue>
) {
  const [sorting, setSorting] = useState<SortingState>([]);

  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
    onSortingChange: setSorting,
    getSortedRowModel: getSortedRowModel(),
    state: {
      sorting,
    },
    ...(pagination?.enabled && { getPaginationRowModel: getPaginationRowModel() }),
  });

  useEffect(() => {
    if (pagination?.enabled) {
      table.setPageSize(pagination.size ?? 10);
    }
  }, [pagination?.enabled, pagination?.size, table]);

  return (
    <div className="flex flex-col gap-1">
      <div className="rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => {
                  return (
                    <TableHead key={header.id}>
                      {header.isPlaceholder
                        ? null
                        : flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )}
                    </TableHead>
                  )
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow
                  key={row.id}
                  data-state={row.getIsSelected() && "selected"}
                >
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>
                      {flexRender(cell.column.columnDef.cell, cell.getContext())}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={columns.length} className="h-24">
                  <div className="flex flex-col gap-1 items-center justify-center">
                    <Image
                      src={gusSadPinIconSrc}
                      alt="data not found"
                      width={100}
                      height={100}
                      className="sm:w-24 sm:h-24 w-16 h-16"
                    />
                    <div className="text-zinc-700 text-sm sm:text-base">
                      통계가 존재하지 않거나 준비중입니다.
                    </div>
                  </div>
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

      {pagination?.enabled &&
        <DataTablePagination table={table} />
      }
    </div>
  )
}

