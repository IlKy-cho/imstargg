"use client";

import {Button} from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import {useState} from "react"
import {ImageSelect} from "@/components/image-select";
import {Gadget} from "@/model/Gadget";
import {uploadGadgetImage} from "@/lib/api/brawler";
import {messagesContent} from "@/lib/message";

interface GadgetImageUploadProps {
  gadget: Gadget;
}

export function GadgetImageUpload({ gadget }: GadgetImageUploadProps) {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  const handleFileSelect = (file: File) => {
    setSelectedFile(file);
  };

  const handleSubmit = async () => {
    if (!selectedFile) return;

    try {
      await uploadGadgetImage(gadget.entity.brawlStarsId, selectedFile);
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
          <DialogTitle>가젯 이미지 업로드</DialogTitle>
          <DialogDescription>
            <p>가젯 이미지를 선택하여 업로드해주세요.</p>
            <p>ID: {gadget.entity.brawlStarsId}</p>
            <p>이름: {messagesContent(gadget.names)}</p>
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