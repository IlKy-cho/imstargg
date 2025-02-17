import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

// 내가 추가한 것

export function cnWithDefault(...inputs: ClassValue[]) {
  return twMerge(clsx("sm:p-2 p-1 border border-zinc-200 rounded", inputs));
}