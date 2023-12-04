package aws;

/*
* Cloud Computing
* 
* Dynamic Resource Management Tool
* using AWS Java SDK Library
* 
*/
import java.util.Iterator;
import java.util.Scanner;

import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DryRunSupportedRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.model.DescribeKeyPairsRequest;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.KeyPairInfo;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.amazonaws.services.ec2.model.DeleteKeyPairRequest;
import com.amazonaws.services.ec2.model.DeregisterImageRequest;
import com.amazonaws.services.ec2.model.CreateImageRequest;
import com.amazonaws.services.ec2.model.CreateImageResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.Image;
import java.util.List;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
public class awsTest {

	static AmazonEC2      ec2;

	private static void init() throws Exception {

		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " +
					"Please make sure that your credentials file is at the correct " +
					"location (~/.aws/credentials), and is in valid format.",
					e);
		}
		ec2 = AmazonEC2ClientBuilder.standard()
			.withCredentials(credentialsProvider)
			.withRegion("ap-northeast-2")	/* check the region at AWS console */
			.build();
	}

	public static void main(String[] args) throws Exception {

		init();

		Scanner menu = new Scanner(System.in);
		Scanner id_string = new Scanner(System.in);
		int number = 0;
		
		while(true)
		{
			System.out.println("                                                            ");
			System.out.println("                                                            ");
			System.out.println("------------------------------------------------------------");
			System.out.println("           Amazon AWS Control Panel using SDK               ");
			System.out.println("------------------------------------------------------------");
			System.out.println("  1. list instance                2. available zones        ");
			System.out.println("  3. start instance               4. available regions      ");
			System.out.println("  5. stop instance                6. create instance        ");
			System.out.println("  7. reboot instance              8. list images            ");
			System.out.println("  9. security group information   10. key pair list         ");
			System.out.println("  11. make key pair               12. delete key pair       ");
			System.out.println("  13. make images                 14. delete images         ");
			System.out.println("                                  98. condor_status          ");
			System.out.println("                                  99. quit                   ");
			System.out.println("------------------------------------------------------------");
			
			System.out.print("Enter an integer: ");
			
			if(menu.hasNextInt()){
				number = menu.nextInt();
				}else {
					System.out.println("concentration!");
					break;
				}
			

			String instance_id = "";
			String keyPair_id = "";
			String ami_Name = "";

			switch(number) {
			case 1: 
				listInstances();
				break;
				
			case 2: 
				availableZones();
				break;
				
			case 3: 
				System.out.print("Enter instance id: ");
				if(id_string.hasNext())
					instance_id = id_string.nextLine();
				
				if(!instance_id.isBlank()) 
					startInstance(instance_id);
				break;

			case 4: 
				availableRegions();
				break;

			case 5: 
				System.out.print("Enter instance id: ");
				if(id_string.hasNext())
					instance_id = id_string.nextLine();
				
				if(!instance_id.isBlank()) 
					stopInstance(instance_id);
				break;

			case 6: 
				System.out.print("Enter ami id: ");
				String ami_id = "";
				if(id_string.hasNext())
					ami_id = id_string.nextLine();
				
				if(!ami_id.isBlank()) 
					createInstance(ami_id);
				break;

			case 7: 
				System.out.print("Enter instance id: ");
				if(id_string.hasNext())
					instance_id = id_string.nextLine();
				
				if(!instance_id.isBlank()) 
					rebootInstance(instance_id);
				break;

			case 8: 
				listImages();
				break;
			case 9:
				securityGroupInfo();
				break;
			case 10:
				keyPairList();
				break;
			case 11:
				System.out.print("Enter keypair id: ");
				if(id_string.hasNext())
					keyPair_id = id_string.nextLine();

				if(!keyPair_id.isBlank())
					makeKeyPair(keyPair_id);
				break;
			case 12:
				System.out.print("Enter keypair id: ");
				if(id_string.hasNext())
					keyPair_id = id_string.nextLine();

				if(!keyPair_id.isBlank())
					deleteKeyPair(keyPair_id);
				break;
			case 13:
				System.out.print("Enter instance id: ");
				if(id_string.hasNext())
					instance_id = id_string.nextLine();

				System.out.print("Enter Ami Name: ");
				if(id_string.hasNext())
					ami_Name = id_string.nextLine();

				if(!instance_id.isBlank() && !ami_Name.isBlank())
					createImage(instance_id,ami_Name);
				break;
			case 14:
				System.out.print("Enter Ami ID: ");
				if(id_string.hasNext())
					ami_Name = id_string.nextLine();

				if(!ami_Name.isBlank())
					deleteImage(ami_Name);
				break;

			case 98:
				startCondor();
				break;

			case 99: 
				System.out.println("bye!");
				menu.close();
				id_string.close();
				return;
			default: System.out.println("concentration!");
			}

		}
		
	}

	public static void listInstances() {
		
		System.out.println("Listing instances....");
		boolean done = false;
		
		DescribeInstancesRequest request = new DescribeInstancesRequest();
		
		while(!done) {
			DescribeInstancesResult response = ec2.describeInstances(request);

			for(Reservation reservation : response.getReservations()) {
				for(Instance instance : reservation.getInstances()) {
					System.out.printf(
						"[id] %s, " +
						"[AMI] %s, " +
						"[type] %s, " +
						"[state] %10s, " +
						"[monitoring state] %s",
						instance.getInstanceId(),
						instance.getImageId(),
						instance.getInstanceType(),
						instance.getState().getName(),
						instance.getMonitoring().getState());
				}
				System.out.println();
			}

			request.setNextToken(response.getNextToken());

			if(response.getNextToken() == null) {
				done = true;
			}
		}
	}
	
	public static void availableZones()	{

		System.out.println("Available zones....");
		try {
			DescribeAvailabilityZonesResult availabilityZonesResult = ec2.describeAvailabilityZones();
			Iterator <AvailabilityZone> iterator = availabilityZonesResult.getAvailabilityZones().iterator();
			
			AvailabilityZone zone;
			while(iterator.hasNext()) {
				zone = iterator.next();
				System.out.printf("[id] %s,  [region] %15s, [zone] %15s\n", zone.getZoneId(), zone.getRegionName(), zone.getZoneName());
			}
			System.out.println("You have access to " + availabilityZonesResult.getAvailabilityZones().size() +
					" Availability Zones.");

		} catch (AmazonServiceException ase) {
				System.out.println("Caught Exception: " + ase.getMessage());
				System.out.println("Reponse Status Code: " + ase.getStatusCode());
				System.out.println("Error Code: " + ase.getErrorCode());
				System.out.println("Request ID: " + ase.getRequestId());
		}
	
	}

	public static void startInstance(String instance_id)
	{
		
		System.out.printf("Starting .... %s\n", instance_id);
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DryRunSupportedRequest<StartInstancesRequest> dry_request =
			() -> {
			StartInstancesRequest request = new StartInstancesRequest()
				.withInstanceIds(instance_id);

			return request.getDryRunRequest();
		};

		StartInstancesRequest request = new StartInstancesRequest()
			.withInstanceIds(instance_id);

		ec2.startInstances(request);

		System.out.printf("Successfully started instance %s", instance_id);
	}
	
	
	public static void availableRegions() {
		
		System.out.println("Available regions ....");
		
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DescribeRegionsResult regions_response = ec2.describeRegions();

		for(Region region : regions_response.getRegions()) {
			System.out.printf(
				"[region] %15s, " +
				"[endpoint] %s\n",
				region.getRegionName(),
				region.getEndpoint());
		}
	}
	
	public static void stopInstance(String instance_id) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		DryRunSupportedRequest<StopInstancesRequest> dry_request =
			() -> {
			StopInstancesRequest request = new StopInstancesRequest()
				.withInstanceIds(instance_id);

			return request.getDryRunRequest();
		};

		try {
			StopInstancesRequest request = new StopInstancesRequest()
				.withInstanceIds(instance_id);
	
			ec2.stopInstances(request);
			System.out.printf("Successfully stop instance %s\n", instance_id);

		} catch(Exception e)
		{
			System.out.println("Exception: "+e.toString());
		}

	}
	
	public static void createInstance(String ami_id) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		
		RunInstancesRequest run_request = new RunInstancesRequest()
			.withImageId(ami_id)
			.withInstanceType(InstanceType.T2Micro)
			.withMaxCount(1)
			.withMinCount(1);

		RunInstancesResult run_response = ec2.runInstances(run_request);

		String reservation_id = run_response.getReservation().getInstances().get(0).getInstanceId();

		System.out.printf(
			"Successfully started EC2 instance %s based on AMI %s",
			reservation_id, ami_id);
	
	}

	public static void rebootInstance(String instance_id) {
		
		System.out.printf("Rebooting .... %s\n", instance_id);
		
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		try {
			RebootInstancesRequest request = new RebootInstancesRequest()
					.withInstanceIds(instance_id);

				RebootInstancesResult response = ec2.rebootInstances(request);

				System.out.printf(
						"Successfully rebooted instance %s", instance_id);

		} catch(Exception e)
		{
			System.out.println("Exception: "+e.toString());
		}

		
	}

	public static void listImages() {
		System.out.println("Listing images....");

		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

		// DescribeImages 요청 생성 및 필터 추가
		DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest()
				.withFilters(
						new Filter("is-public").withValues("false")
				);

		try {
			// DescribeImages API 호출
			DescribeImagesResult describeImagesResult = ec2.describeImages(describeImagesRequest);
			List<Image> images = describeImagesResult.getImages();

			// 출력
			System.out.println("AMI Images:");
			for (Image image : images) {
				System.out.println("Image ID: " + image.getImageId() +
						", Name: " + image.getName() +
						", State: " + image.getState());
			}
		} catch (Exception e) {
			System.err.println("Error listing AMI images: " + e.getMessage());
		}

	}

	public static void securityGroupInfo(){
		// 보안 그룹 목록 조회 요청 생성
		DescribeSecurityGroupsRequest describeSecurityGroupsRequest = new DescribeSecurityGroupsRequest();

		try {
			// 보안 그룹 목록 조회
			DescribeSecurityGroupsResult securityGroupsResult =
					ec2.describeSecurityGroups(describeSecurityGroupsRequest);

			// 조회 결과 출력
			for (SecurityGroup securityGroup : securityGroupsResult.getSecurityGroups()) {
				System.out.println("Security Group Id: " + securityGroup.getGroupId());
				System.out.println("Security Group Name: " + securityGroup.getGroupName());
				System.out.println("Description: " + securityGroup.getDescription());
				System.out.println("VPC Id: " + securityGroup.getVpcId());
				System.out.println("Inbound Rules: " + securityGroup.getIpPermissions());
				System.out.println("Outbound Rules: " + securityGroup.getIpPermissionsEgress());
				System.out.println("----------------------------------");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void keyPairList(){
		// 키페어 목록 조회 요청 생성
		DescribeKeyPairsRequest describeKeyPairsRequest = new DescribeKeyPairsRequest();

		try {
			// 키페어 목록 조회
			DescribeKeyPairsResult keyPairsResult = ec2.describeKeyPairs(describeKeyPairsRequest);

			// 조회 결과 출력
			for (KeyPairInfo keyPairInfo : keyPairsResult.getKeyPairs()) {
				System.out.println("Key Pair Name: " + keyPairInfo.getKeyName());
				System.out.println("Key Pair Fingerprint: " + keyPairInfo.getKeyFingerprint());
				System.out.println("----------------------------------");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void makeKeyPair(String keyPairName) {

		// 키페어 생성 요청 생성
		CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest()
				.withKeyName(keyPairName);

		try {
			// 키페어 생성
			CreateKeyPairResult keyPairResult = ec2.createKeyPair(createKeyPairRequest);

			// 키페어의 키 값 가져오기
			String privateKey = keyPairResult.getKeyPair().getKeyMaterial();

			// 키페어 키 값을 파일로 저장 (옵션)
			savePrivateKeyToFile(privateKey, "/home/seyeon/Downloads/" + keyPairName + ".pem");

			System.out.println("Key Pair created successfully.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void savePrivateKeyToFile(String privateKey, String filePath) {
		try {
			Path path = Paths.get(filePath);
			Files.write(path, privateKey.getBytes());
			System.out.println("Private key saved to: " + filePath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startCondor(){
		// SSH 연결 정보
		String host = "*****"; // EC2 인스턴스의 IP 주소
		String username = "*******"; // SSH로 접속할 계정
		String privateKeyPath = "***"; // SSH 키 파일의 경로

		try {
			// JSch 초기화
			JSch jsch = new JSch();
			jsch.addIdentity(privateKeyPath);

			// 세션 열기
			Session session = jsch.getSession(username, host, 22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			// 명령 실행
			String command = "condor_status"; // 실행할 명령
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// 명령 실행 결과 읽기
			InputStream commandOutput = channel.getInputStream();
			channel.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(commandOutput));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			// 연결 종료
			channel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteKeyPair(String keyName){
		// 키페어 이름 설정
		String keyPairName = keyName;

		// DeleteKeyPair 요청 생성
		DeleteKeyPairRequest deleteKeyPairRequest = new DeleteKeyPairRequest().withKeyName(keyPairName);

		try {
			// DeleteKeyPair API 호출
			ec2.deleteKeyPair(deleteKeyPairRequest);
			System.out.println("Key pair deleted successfully");
		} catch (Exception e) {
			System.err.println("Error deleting key pair: " + e.getMessage());
		}
	}

	public static void createImage(String instanceId, String amiName){

		// CreateImage 요청 생성
		CreateImageRequest createImageRequest = new CreateImageRequest()
				.withInstanceId(instanceId)
				.withName(amiName);

		try {
			// CreateImage API 호출
			CreateImageResult createImageResult = ec2.createImage(createImageRequest);
			String amiId = createImageResult.getImageId();
			System.out.println("AMI created successfully with ID: " + amiId);
		} catch (Exception e) {
			System.err.println("Error creating AMI: " + e.getMessage());
		}
	}

	public static void deleteImage(String amiId){

		// DeregisterImage 요청 생성
		DeregisterImageRequest deregisterImageRequest = new DeregisterImageRequest().withImageId(amiId);

		try {
			// DeregisterImage API 호출
			ec2.deregisterImage(deregisterImageRequest);
			System.out.println("AMI deregistered successfully");
		} catch (Exception e) {
			System.err.println("Error deregistering AMI: " + e.getMessage());
		}
	}


}
	
