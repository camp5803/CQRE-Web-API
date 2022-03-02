package sch.cqre.api.service;

import static sch.cqre.api.exception.ErrorCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import sch.cqre.api.domain.UserEntity;
import sch.cqre.api.exception.CustomException;
import sch.cqre.api.repository.UserRepository;

@Service
@AllArgsConstructor
public class AccountService {
	private final UserRepository userRepo;

	public UserEntity searchByEmail(String email) {
		return this.userRepo.findByEmail(email)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
	}

	@Transactional
	public UserEntity setEmail(Integer userId, String email) {
		UserEntity userEntity = this.userRepo.findById(userId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		userEntity.setEmail(email);
		return userEntity;
	}
	public void changePassword(Integer userId) {}
	public void withdrawal(Integer userId) {
		this.userRepo.deleteById(userId);
	}
}