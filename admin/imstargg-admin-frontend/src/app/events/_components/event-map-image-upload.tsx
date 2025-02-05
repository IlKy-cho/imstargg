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
import Image from "next/image"
import BattleEvent from "@/model/BattleEvent";
import {uploadMapImage} from "@/lib/api/event";

interface MapImageUploadProps {
  battleEvent: BattleEvent;
}

export function EventMapImageUpload({ battleEvent }: MapImageUploadProps) {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setSelectedFile(file);
      
      // 이미지 미리보기 URL 생성
      const url = URL.createObjectURL(file);
      setPreviewUrl(url);
    }
  };

  const handleSubmit = async () => {
    if (!selectedFile) return;
    if (!battleEvent.entity.brawlStarsId) {
      throw new Error("이벤트의 Brawl Stars ID가 없습니다.");
    }

    try {
      await uploadMapImage(battleEvent.entity.brawlStarsId, selectedFile);
      window.location.reload();
    } catch (error) {
      console.error("이미지 업로드 실패:", error);
    }
  };

  // 컴포넌트가 언마운트될 때 미리보기 URL 해제
  const handleClose = () => {
    if (previewUrl) {
      URL.revokeObjectURL(previewUrl);
      setPreviewUrl(null);
    }
    setSelectedFile(null);
  };

  return (
    <Dialog onOpenChange={(open) => !open && handleClose()}>
      <DialogTrigger asChild>
        <Button variant="outline" size="sm">이미지 업로드</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>{battleEvent.entity.brawlStarsId} 맵 이미지 업로드</DialogTitle>
          <DialogDescription>
            맵의 이미지를 선택하여 업로드해주세요.
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
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