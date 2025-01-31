import { LoaderIcon } from "lucide-react";

export default function Loading() {
  return (
    <div className="flex items-center justify-center min-h-screen">
      <LoaderIcon className="animate-spin w-8 h-8 sm:w-12 sm:h-12" />
    </div>
  );
} 