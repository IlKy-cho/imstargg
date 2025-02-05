"use client";

import { useState } from "react";
import Image from "next/image";

interface ImageSelectProps {
  onFileSelect: (file: File) => void;
}

export function ImageSelect({ onFileSelect }: ImageSelectProps) {
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];

      onFileSelect(file);

      if (previewUrl) {
        URL.revokeObjectURL(previewUrl);
      }

      const url = URL.createObjectURL(file);
      setPreviewUrl(url);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center">
      <input
        type="file"
        accept="image/*"
        onChange={handleFileChange}
        className="mb-4"
      />
      {previewUrl && (
        <div className="relative w-full h-[200px]">
          <Image
            src={previewUrl}
            alt="미리보기"
            fill
            className="object-contain"
          />
        </div>
      )}
    </div>
  );
}