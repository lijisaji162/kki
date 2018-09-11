package com.polus.fibicomp.constants;

public interface Constants {

	String ALL_SPONSOR_HIERARCHY_NIH_MULTI_PI = "ALL_SPONSOR_HIERARCHY_NIH_MULTI_PI";
	String KC_GENERIC_PARAMETER_NAMESPACE = "KC-GEN";
	String KC_B_PARAMETER_NAMESPACE = "KC-B";
	String KC = "KC";
	String KC_ALL_PARAMETER_DETAIL_TYPE_CODE = "All";
	String KC_DOC_PARAMETER_DETAIL_TYPE_CODE = "Document";
	String SPONSOR_HIERARCHIES_PARM = "PERSON_ROLE_SPONSOR_HIERARCHIES";
	String NIH_MULTIPLE_PI_HIERARCHY = "NIH Multiple PI";
	String DEFAULT_SPONSOR_HIERARCHY_NAME = "DEFAULT";
	String INVALID_TIME = "Invalid Time";
	String ALTERNATE_ROLE = "12";
	String INACTIVE_ROLE = "14";
	String DEFAULTTIME = "12:00";
	String NAME = "t";
	String GROUP = "g";
	String JOBNAME = "j";
	String JOBGROUP = "g";
	String COLON = ":";
	String ZERO = "0";
	String PRINCIPAL_INVESTIGATOR = "PI";
	String MULTI_PI = "MPI";
	String CO_INVESTIGATOR = "COI";
	String KEY_PERSON = "KP";
	String DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION = "Y";
	String DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION = "N";
	String ATTENDANCE = "2";
	String PROTOCOL = "3";
	String ACTION_ITEM = "4";
	String PROTOCOL_REVIEWER_COMMENT = "6";
	String DESCRIPTION = "description";
	String HASH_ALGORITHM = "SHA";
	String CHARSET = "UTF-8";
	String DECIMAL_FORMAT = "00000000";
	String DECIMAL_FORMAT_FOR_NEW_IP = "0000";
	String INSTITUTIONAL_PROPSAL_PROPSAL_NUMBER_SEQUENCE = "SEQ_PROPOSAL_PROPOSAL_ID";
	String DEFAULT_HOME_UNIT_NUMBER = "000001";
	String DEFAULT_HOME_UNIT_NAME = "University";

	// Security constants
	String SECRET = "SecretKeyToGenJWTs";
	long EXPIRATION_TIME = 864_000_000; // 10 days
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";
	String SIGN_UP_URL = "/fibi-comp/login";

	// Grant Call
	Integer GRANT_CALL_STATUS_CODE_DRAFT = 1;
	Integer GRANT_CALL_STATUS_CODE_OPEN = 2;
	Integer GRANT_CALL_TYPE_INTERNAL = 1;
	Integer GRANT_CALL_TYPE_EXTERNAL = 2;
	Integer GRANT_CALL_TYPE_OTHERS = 3;

	// Proposal
	Integer PROPOSAL_STATUS_CODE_IN_PROGRESS = 1;
	Integer PROPOSAL_STATUS_CODE_SUBMITTED = 5;
	Integer PROPOSAL_STATUS_CODE_APPROVED = 4;
	Integer PROPOSAL_STATUS_CODE_REJECTED = 3;
	Integer PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS = 2;
	Integer PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS = 8;
	Integer PROPOSAL_STATUS_CODE_REVISION_REQUESTED = 9;
	Integer PROPOSAL_STATUS_CODE_ENDORSEMENT = 10;
	Integer PROPOSAL_STATUS_CODE_AWARDED = 11;

	// Route Log Status Code
	String WORKFLOW_STATUS_CODE_WAITING = "W";
	String WORKFLOW_STATUS_CODE_APPROVED = "A";
	String WORKFLOW_STATUS_CODE_REJECTED = "R";
	String WORKFLOW_STATUS_CODE_TO_BE_SUBMITTED = "T";
	String WORKFLOW_STATUS_CODE_WAITING_FOR_REVIEW = "WR";
	String WORKFLOW_STATUS_CODE_REVIEW_COMPLETED = "RC";

	// Workflow first stop number
	Integer WORKFLOW_FIRST_STOP_NUMBER = 1;
	Integer REVIEWER_ROLE_TYPE_CODE = 3;
	Integer ADMIN_ROLE_TYPE_CODE = 2;
	String SMU_GRANT_MANAGER_CODE = "10";
	String SMU_GRANT_PROVOST_CODE = "11";

	// Protocol
	String PROTOCOL_SATUS_CODE_ACTIVE_OPEN_TO_ENTROLLMENT = "200";

	// Parameter Constants
	String MODULE_NAMESPACE_PROPOSAL_DEVELOPMENT = "KC-PD";
	String DOCUMENT_COMPONENT = "Document";
	String FISCAL_YEAR_BASED_IP = "GENERATE_IP_BASED_ON_FY";

	// OST Status
	Integer STATUS_AT_OSP_CODE = 1;
	Integer STATUS_AT_DLC_NOT_SUBMITTED_CODE = 9;
	Integer STATUS_PROCESSING_CODE = 7;
	String STATUS_AT_DLC_NOT_SUBMITTED = "DLC-Not Submitted";
	Integer ACTION_NEW_REQUEST = 8;
	Integer ACTION_NEW_REPORTER = 10;
	Integer STATUS_AT_POPS_CODE = 4;
	Integer POPS_CATEGORY_CODE = 5;
	Integer ACTION_SUBMIT = 15;
	Integer OST_PRIORITY_NORMAL = 2;

	// Budget Constants
	String DEFAULT_RATE_CLASS_CODE = "defaultOverheadRateClassCode";
	String DEFAULT_RATE_TYPE_CODE = "defaultOverheadRateTypeCode";
	String DEFAULT_RATE_CLASS_TYPE_CODE = "defaultOverheadRateClassTypeCode";

	public static final String STRING_TO_DATE_FORMATS = "STRING_TO_DATE_FORMATS";
    public static final String STRING_TO_TIME_FORMATS = "STRING_TO_TIME_FORMATS";
    public static final String STRING_TO_TIMESTAMP_FORMATS = "STRING_TO_TIMESTAMP_FORMATS";
    public static final String DATE_TO_STRING_FORMAT_FOR_USER_INTERFACE = "DATE_TO_STRING_FORMAT_FOR_USER_INTERFACE";
    public static final String TIME_TO_STRING_FORMAT_FOR_USER_INTERFACE = "TIME_TO_STRING_FORMAT_FOR_USER_INTERFACE";
    public static final String TIMESTAMP_TO_STRING_FORMAT_FOR_USER_INTERFACE = "TIMESTAMP_TO_STRING_FORMAT_FOR_USER_INTERFACE";
    public static final String DATE_TO_STRING_FORMAT_FOR_FILE_NAME = "DATE_TO_STRING_FORMAT_FOR_FILE_NAME";
    public static final String TIMESTAMP_TO_STRING_FORMAT_FOR_FILE_NAME = "TIMESTAMP_TO_STRING_FORMAT_FOR_FILE_NAME";

    //System Generated CostElemets Details
  	public static final String  BUDGET_RESEARCH_OH_ON = "BUDGET_RESEARCH_OH_ON";
  	public static final String  BUDGET_RESEARCH_OH_OFF = "BUDGET_RESEARCH_OH_OFF";
  	public static final String  BUDGET_OH_ON = "BUDGET_OH_ON";
  	public static final String  BUDGET_OH_OFF = "BUDGET_OH_OFF";
  	public static final String  BUDGET_FRINGE_ON = "BUDGET_FRINGE_ON";
  	public static final String  BUDGET_FRINGE_OFF = "BUDGET_FRINGE_OFF";

  	// Enable Required Section
  	public static final String  IS_REQUIRED_DECLARATION_SECTION = "IS_REQUIRED_DECLARATION_SECTION";

}
