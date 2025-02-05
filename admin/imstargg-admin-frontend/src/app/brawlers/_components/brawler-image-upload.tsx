"use client";

import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { useState } from "react"
import {uploadBrawlerProfileImage} from "@/lib/api/brawler";
import { ImageSelect } from "@/components/image-select";

interface BrawlerImageUploadProps {
  brawlStarsId: number;
}

export function BrawlerImageUpload({ brawlStarsId }: BrawlerImageUploadProps) {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  const handleFileSelect = (file: File) => {
    setSelectedFile(file);
  };

  const handleSubmit = async () => {
    if (!selectedFile) return;
    
    try {
      await uploadBrawlerProfileImage(brawlStarsId, selectedFile);
      window.location.reload();
    } catch (error) {
      console.error("이미지 업로드 실패:", error);
    }
  };

  const handleClose = () => {
    setSelectedFile(null);
  };

  return (
    <Dialog onOpenChange={(open) => !open && handleClose()}>
      <DialogTrigger asChild>
        <Button variant="outline" size="sm">이미지 업로드</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>브롤러 이미지 업로드</DialogTitle>
          <DialogDescription>
            브롤러의 프로필 이미지를 선택하여 업로드해주세요.
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <ImageSelect onFileSelect={handleFileSelect} />
        </div>
        <DialogFooter>
          <Button 
            type="submit" 
            onClick={handleSubmit}
            disabled={!selectedFile}
          >
            업로드
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
} 