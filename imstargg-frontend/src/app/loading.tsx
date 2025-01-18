import { LoaderIcon } from "lucide-react";

export default function Loading() {
  return (
    <div className="flex items-center justify-center min-h-screen">
      <LoaderIcon className="animate-spin w-12 h-12" />
    </div>
  );
} 