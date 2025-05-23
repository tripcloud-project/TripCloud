package com.ssafy.project.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.project.common.response.ApiResponse;
import com.ssafy.project.domain.auth.exception.InvalidTokenException;
import com.ssafy.project.domain.board.exception.CommentDeletionNotAllowedException;
import com.ssafy.project.domain.board.exception.CommentInsertNotAllowedException;
import com.ssafy.project.domain.board.exception.NotFoundPostException;
import com.ssafy.project.domain.board.exception.PostDeletionNotAllowedException;
import com.ssafy.project.domain.board.exception.PostUpdateNotAllowedException;
import com.ssafy.project.domain.gallery.exception.DownloadFailException;
import com.ssafy.project.domain.gallery.exception.FileNotFoundException;
import com.ssafy.project.domain.gallery.exception.InvalidRegionException;
import com.ssafy.project.domain.gallery.exception.RenameFailException;
import com.ssafy.project.domain.gallery.exception.SetThumbnailNotAllowedException;
import com.ssafy.project.domain.gallery.exception.UpdateDescriptionNotAllowedException;
import com.ssafy.project.domain.gallery.exception.UploadFailException;
import com.ssafy.project.domain.member.exception.DuplicateMemberException;
import com.ssafy.project.domain.member.exception.InvalidPasswordException;
import com.ssafy.project.domain.member.exception.NotFoundMemberException;

import io.jsonwebtoken.JwtException;


// 예외가 발생하면 이 클래스에서 담당.
@RestControllerAdvice(annotations = RestController.class) // @RestController Annotation이 붙은 모든 클래스 대상.
public class ControllerExceptionHandler {

    private ResponseEntity<?> errorResponse(ErrorCode code) {
        ApiResponse<?> errorResponse = ApiResponse.createError(code.getMessage());
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(errorResponse);
    }

    // 어떤 예외가 발생하면 어떻게 응답을 해줄지.
    @ExceptionHandler(value = {NotFoundMemberException.class})
    public ResponseEntity<?> notFoundUserException() {
        return errorResponse(ErrorCode.NOT_FOUND_USER);
    }

    // 키가 중복되는 멤버 삽입 시도
    @ExceptionHandler(value = {DuplicateMemberException.class})
    public ResponseEntity<?> duplicateUserException() {
        return errorResponse(ErrorCode.DUPLICATE_MEMBER);
    }

    // 유효하지 않은 비밀번호 입력
    @ExceptionHandler(value = {InvalidPasswordException.class})
    public ResponseEntity<?> invalidPasswordException() {
        return errorResponse(ErrorCode.INVALID_PASSWORD);
    }

    // 파일 업로드 예외
    @ExceptionHandler(UploadFailException.class)
    public ResponseEntity<?> handleUploadFailException(UploadFailException e) {
        return errorResponse(ErrorCode.UPLOAD_FAIL);
    }

    // 파일 이름 변경 실패
    @ExceptionHandler(RenameFailException.class)
    public ResponseEntity<?> renameFailException(RenameFailException e) {
        return errorResponse(ErrorCode.RENAME_FAIL);
    }

    // 파일 조회 실패
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> photoNotFoundException(FileNotFoundException e){
    	return errorResponse(ErrorCode.PHOTO_NOT_FOUND);    	
    }

    // 파일 다운로드 실패
    @ExceptionHandler(DownloadFailException.class)
    public ResponseEntity<?> downloadFailException(DownloadFailException e) {
        return errorResponse(ErrorCode.DOWNLOAD_FAIL);
    }

    // 게시글 삭제 실패
    @ExceptionHandler(PostDeletionNotAllowedException.class)
    public ResponseEntity<?> postDeletionNotAllowedException(PostDeletionNotAllowedException e) {
        return errorResponse(ErrorCode.POST_DELETE_NOT_ALLOWED);
    }

    // 유효하지 않은 토큰 사용
    @ExceptionHandler({InvalidTokenException.class, JwtException.class})
    public ResponseEntity<?> invalidTokenException() {
        return errorResponse(ErrorCode.INVALID_TOKEN);
    }
    
    // 댓글 등록 실패
    @ExceptionHandler(CommentInsertNotAllowedException.class)
    public ResponseEntity<?> commentInsertNotAllowedException(){
    	return errorResponse(ErrorCode.COMMENT_CREATE_FAIL);
    }
    
    // 게시글 조회 실패
    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<?> notFoundPostException(){
    	return errorResponse(ErrorCode.POST_NOT_FOUND);
    }

    // 댓글 삭제 실패
    @ExceptionHandler(CommentDeletionNotAllowedException.class)
    public ResponseEntity<?> commentDeletionNotAllowedException(){
        return errorResponse(ErrorCode.COMMENT_DELETE_NOT_ALLOWED);
    }

    // 게시글 수정 실패
    @ExceptionHandler(PostUpdateNotAllowedException.class)
    public ResponseEntity<?> postUpdateNotAllowedException(){
        return errorResponse(ErrorCode.POST_UPDATE_NOT_ALLOWED);
    }
    
    // 파일 설명 수정 실패
    @ExceptionHandler(UpdateDescriptionNotAllowedException.class)
	public ResponseEntity<?> updateDescriptionNotAllowedException(){
		return errorResponse(ErrorCode.PHOTO_UPDATE_DESCRIPTION_NOT_ALLOWED);
	}
    
    // 지역 선택 오류
    @ExceptionHandler(InvalidRegionException.class)
    public ResponseEntity<?> invalidRegionException(){
    	return errorResponse(ErrorCode.INVALID_REGION);
    }
    
    // 대표 사진 수정 실패

    @ExceptionHandler(SetThumbnailNotAllowedException.class)
    public ResponseEntity<?> setThumbnailNotAllowedException(){
    	return errorResponse(ErrorCode.THUMBNAIL_SET_NOT_ALLOWED);
    }
}
