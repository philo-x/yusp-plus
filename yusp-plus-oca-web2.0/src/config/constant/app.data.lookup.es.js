/**
 * @created by helin3 2019-04-11
 * @description 静态数据字典
 */

export default {
  CRUD_TYPE: [
    { key: 'ADD', value: 'Add' },
    { key: 'EDIT', value: 'Edit' },
    { key: 'DETAIL', value: 'Detail' }
  ],
  GENDER: [
    { key: '01', value: 'male' },
    { key: '02', value: 'female' }
  ],
  YESNO: [
    { key: '01', value: 'yes' },
    { key: '02', value: 'no' }
  ],
  HASNO: [
    { key: '01', value: 'have' },
    { key: '02', value: 'no' }
  ],
  OBJECT_TYPE: [
    { 'key': 'R', 'value': 'role' },
    { 'key': 'U', 'value': 'user' },
    { 'key': 'D', 'value': 'department' },
    { 'key': 'G', 'value': 'institutions' }
  ],
  NOTICE_LEVEL: [
    { 'key': 'I', 'value': 'important' },
    { 'key': 'N', 'value': 'common' }
  ],
  TOP_FLAG: [
    { 'key': 'I', 'value': 'yes' },
    { 'key': 'N', 'value': 'no' }
  ],
  USER_CLASSIFY: [
    { 'key': 'gg', 'value': 'Notice' },
    { 'key': 'kx', 'value': 'Industry News' }
  ],
  USER_STATUS: [
    { 'key': '01', 'value': 'take effect' },
    { 'key': '03', 'value': 'invalid' },
    { 'key': '02', 'value': 'To be effective' }
  ],
  DEPARTMENT: [
    { key: 't01', value: 'Technology Department 1' },
    { key: 't02', value: 'Technology Department 2' },
    { key: 'board_director', value: 'Board of directors' },
    { key: 't03', value: 'Technology Department 3' },
    { key: 't04', value: 'Technology Department 4' }
  ],
  BRANCH: [
    { key: 'c1001', value: 'Chengdu sub branch' },
    { key: 'c1002', value: 'Beijing headquarters' }
  ],
  EDUCATION_TYPE: [
    { key: '0', value: 'PhD' },
    { key: '1', value: 'Master' },
    { key: '2', value: 'Bachelor Degree' },
    { key: '3', value: 'junior college' },
    { key: '4', value: 'Senior high school and below' }
  ],
  NATION: [
    { key: '01', value: 'Han nationality' },
    { key: '02', value: 'Dai nationality' },
    { key: '03', value: 'Uygur ethnic group' },
    { key: '04', value: 'Bai nationality' }
  ],
  COMPANY_TYPE: [
    { key: '01', value: 'State-owned enterprise' },
    { key: '02', value: 'Collective enterprises' },
    { key: '03', value: 'Graduate student' },
    { key: '04', value: 'Private enterprise' },
    { key: '05', value: 'Individual businesses' }
  ],
  JOB_TYPE: [
    { key: '01', value: 'Management position' },
    { key: '02', value: 'Professional Post' },
    { key: '03', value: 'Work post' }
  ],
  RESOURCE_TYPE: [
    { 'key': 'M', 'value': 'menu' },
    { 'key': 'C', 'value': 'The control points' },
    { 'key': 'D', 'value': 'Data access' }
  ],
  RECIVE_TYPE: [
    { 'key': 'R', 'value': 'role' },
    { 'key': 'G', 'value': 'institutions' }
  ],
  PUB_STS: [
    { 'key': 'O', 'value': 'Published' },
    { 'key': 'C', 'value': 'Unpublished' }
  ],
  OP_TYPE: [
    { 'key': 'O-0', 'value': 'Get back'},
    { 'key': 'O-1', 'value': 'Repulse'},
    { 'key': 'O-2', 'value': 'Return'},
    { 'key': 'O-3', 'value': 'Hang up'},
    { 'key': 'O-4', 'value': 'awaken'},
    { 'key': 'O-5', 'value': 'Urging'},
    { 'key': 'O-6', 'value': 'Transfer'},
    { 'key': 'O-7', 'value': 'Co Organizer'},
    { 'key': 'O-8', 'value': 'Veto'},
    { 'key': 'O-9', 'value': 'Jump'},
    { 'key': 'O-10', 'value': 'Entrust'},
    { 'key': 'O-11', 'value': 'Copy'},
    { 'key': 'O-12', 'value': 'Agree'},
    { 'key': 'O-13', 'value': 'Auto submit'},
    { 'key': 'O-14', 'value': 'Normal end'},
    { 'key': 'O-15', 'value': 'WithdraW'},
    { 'key': 'O-16', 'value': 'Launch'}
  ],
  DATA_STS: [
    {'key': 'A', 'value': 'Valid'},
    {'key': 'I', 'value': 'Invalid'},
    {'key': 'W', 'value': 'To be Effective'}
  ],
  REL_STS: [
    {'key': true, 'value': 'Related'},
    {'key': false, 'value': 'UnRelated'}
  ],
  HTTP_METHOD_TYPE: [
    {'key': 'POST', 'value': 'POST'},
    {'key': 'GET', 'value': 'GET'}
  ],
  SEX_TYPE: [
    {'key': '0', 'value': 'Female'},
    {'key': '1', 'value': 'Male'},
    {'key': '2', 'value': 'Unknown'}
  ],
  IDENT_TYPE: [
    {'key': 'A', 'value': 'Domestic Enterprise Code'},
    {'key': 'D', 'value': 'Taiwan ID Card'},
    {'key': 'K', 'value': 'Union Organization Code'},
    {'key': 'M', 'value': 'Passport of Domestic Resident'},
    {'key': 'F', 'value': 'Passport of Overseas Resident'},
    {'key': 'H', 'value': 'Approval No. of Name of Domestic Enterprise'},
    {'key': 'B', 'value': 'Code of Overseas Institution'},
    {'key': 'C', 'value': 'ID Card of Somestic Resident'},
    {'key': 'E', 'value': 'ID Card of Hong Kong and Macao Residents'},
  ],
  HIGHEST_EDU: [
    {'key': '1', 'value': 'Postdoctoral'},
    {'key': '2', 'value': 'PhD'},
    {'key': '3', 'value': 'Master'},
    {'key': '4', 'value': 'Bachelor Degree'},
    {'key': '5', 'value': 'Collage/Radio and Television University Degree'},
    {'key': '6', 'value': 'Secondary School Education'},
    {'key': '7', 'value': 'Technical college'},
    {'key': '8', 'value': 'High School'},
    {'key': '9', 'value': 'Junior high school'},
    {'key': '10', 'value': 'Primary school'},
    {'key': '11', 'value': 'Illiterate or semi-literate'},
    {'key': '99', 'value': 'Other'},
  ],
  MESSAGE_LEVEL: [
    {'key': 'info', 'value': 'Info'},
    {'key': 'M001', 'value': 'Unknown Error'},
    {'key': 'success', 'value': 'Success'},
    {'key': 'error', 'value': 'Error'},
  ],
  MESSAGE_TYPE: [
    {'key': 'MODULEINFO', 'value': 'Module Prompt'},
    {'key': 'COMINFO', 'value': 'System Level General Tips'},
    {'key': 'DBERR', 'value': 'System Level General Tips'},
  ],
  LOG_TYPE: [
    {'key': '2', 'value': 'Query Data Log'},
    {'key': '5', 'value': 'Logout System'},
    {'key': '9', 'value': 'Authorization Log'},
    {'key': '7', 'value': 'Access Menu'},
    {'key': '1', 'value': 'Manipulating User'},
    {'key': '6', 'value': 'Operation Log'},
    {'key': '8', 'value': 'Access Report'},
    {'key': '10', 'value': 'Export Repor'},
    {'key': '3', 'value': 'Login Sys'},
    {'key': '4', 'value': 'Log of Button Operation'}
  ],
  READ_STS: [
    {'key': '1', 'value': 'Read'},
    {'key': '0', 'value': 'Unread'},
  ],
  PASSWD_COMPLEX_RULE: [
    {'key': 'number', 'value': 'number'},
    {'key': 'lowercase', 'value': 'lowercase'},
    {'key': 'uppercase', 'value': 'uppercase'},
    {'key': 'specialCharacters', 'value': 'specialCharacters'},
  ]
}
