import {Column} from "@tanstack/react-table"
import {ArrowDown, ArrowUp, ChevronsUpDown} from "lucide-react"

import {cn} from "@/lib/utils"
import {Button} from "@/components/ui/button"


interface DataTableColumnHeaderProps<TData, TValue>
  extends React.HTMLAttributes<HTMLSpanElement> {
  column: Column<TData, TValue>
  title: string
}

export function DataTableColumnHeader<TData, TValue>({
  column,
  title,
  className
}: DataTableColumnHeaderProps<TData, TValue>) {
  if (!column.getCanSort()) {
    return <span className={cn(className)}>{title}</span>
  }

  return (
    <Button
      variant="ghost"
      onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
      className="-ml-3 h-8 data-[state=open]:bg-accent"
    >
      <span className={cn(className)}>{title}</span>
      {column.getIsSorted() === "desc" ? (
        <ArrowDown className="ml-1 h-4 w-4" />
      ) : column.getIsSorted() === "asc" ? (
        <ArrowUp className="ml-1 h-4 w-4" />
      ) : (
        <ChevronsUpDown className="ml-1 h-4 w-4" />
      )}
    </Button>
  )
}
