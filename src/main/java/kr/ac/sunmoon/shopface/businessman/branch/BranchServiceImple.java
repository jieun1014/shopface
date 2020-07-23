package kr.ac.sunmoon.shopface.businessman.branch;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

import kr.ac.sunmoon.shopface.employ.Employ;
import kr.ac.sunmoon.shopface.employ.EmployMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RequiredArgsConstructor
@Service
public class BranchServiceImple implements BranchService {
	private final BranchMapper branchMapper;
	private final EmployMapper employMapper;
	private final AmazonS3 awsS3Client;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	@Override
	public boolean addBranch(Branch branch) {
		boolean result = false;
		try {
			if (branch.getMemberId() != null
					&& !"".equals(branch.getMemberId())
					&& branch.getName() != null
					&& !"".equals(branch.getName())
					&& branch.getPhone() != null
					&& !"".equals(branch.getPhone())
					&& branch.getAddress() != null
					&& !"".equals(branch.getAddress())
					&& branch.getDetailAddress() != null
					&& !"".equals(branch.getDetailAddress())
					&& branch.getZipCode() != null
					&& !"".equals(branch.getZipCode())) {
				this.branchMapper.insert(branch);
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			return result;
		}
	}

	@Override
	public List<Branch> getBranchList(Branch branch) {
		if (branch != null) {
			if ("admin".equals(branch.getMemberId())) {
				branch.setMemberId(null);
				
				return this.branchMapper.selectAll(branch);
			}
			
			return this.branchMapper.selectAll(branch);
		}
		
		return null;
	}
	
	public Branch getBranch(int no) {
		//지점 조회한다
		Branch branch = new Branch();
		try {
			//1. 입력 값 조회(일련번호 0 이상)
			if (no > 0) {
				branch = this.branchMapper.select(no);
				if(branch == null) {
				}
				return branch;				
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			//2. 조회 중 오류 발생 시 널값 반환
			return null;
		}
	}

	@Override
	public boolean editBranch(Branch branch, MultipartFile licenseImage) throws IOException {
		//1. 입력값 검증
		try {
			if (branch.getNo() != 0
					&& branch.getMemberId() != null
					&& !"".equals(branch.getMemberId())
					&& branch.getName() != null
					&& !"".equals(branch.getName())) {
				//2. 존재 시 지점 정보 수정 후 true값 밪환
				if (licenseImage != null) {
					String fileName = licenseImage.getOriginalFilename();
					awsS3Client.putObject(new PutObjectRequest(this.bucket, fileName, licenseImage.getInputStream(), null));
					
					String url = awsS3Client.getUrl(this.bucket, fileName).toString();
					
					branch.setBusinessLPath(awsS3Client.getUrl(this.bucket, fileName).toString());
				}
				this.branchMapper.update(branch);
				return true;
			} else if (branch.getNo() != 0 
					&& !"".equals("" + branch.getApprovalStatus())){
				this.branchMapper.update(branch);
				
				return true;
			}
		} catch(Exception e) {
			//3. 존재 안할 시 false값 반환
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public boolean removeBranch(Branch branch) {
		try{
			//1. 입력 값 검증
			if (branch.getNo() > 0
					&& branch.getMemberId() != null
					&& !"".equals(branch.getMemberId())){
				//2. 입력 값 검증 완료 시 지점 조회
				Branch result = this.branchMapper.select(branch.getNo());
				//3. 사업자 본인이 등록한 지점인지 확인 (전달받은 사업자id와 조회한 지점의 사업자id가 같은지 비교)
				if (result != null) { //지점 조회 시 결과가 있는지 검사
					String resultId = result.getMemberId();
					if (resultId.equals(branch.getMemberId())) {
						//4. 같을 시 고용 목록 조회 (지점의 일련번호로 조회)
						//5. 연관된 고용관계가 있는지 확인(지점 일련 번호와 고용정보의 지점 일련번호 일치 여부 확인)
						Employ employ = new Employ();
						employ.setBranchNo(branch.getNo());
						
						List<Employ> employs = this.employMapper.selectAll(employ);
						if (employs != null && employs.size() >= 1) {
							//6. 연결된 고용 정보 존재 시 삭제하지 않고 비활성화
							branch.setState('D');
							this.branchMapper.update(branch);
							return true;
						} else {
							//7. 연결된 고용 정보 미존재 시 삭제
							this.branchMapper.delete(branch);
							return true;
						} 
					} return false;
				} return false;
			} return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
