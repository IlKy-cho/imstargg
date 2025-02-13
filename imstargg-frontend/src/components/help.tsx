import {InfoIcon} from "lucide-react";

export function Help({description}: Readonly<{description: string}>) {
  return (
    <div className="flex items-center gap-1 text-zinc-700 sm:text-sm text-xs">
      <InfoIcon className="sm:w-5 w-4"/>
      <p>{description}</p>
    </div>
  );
}