package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartResolver;

import bean.Attendance;
import bean.AuthMember;
import bean.SubJoinMem;
import bean.Subject;
import command.AttendanceInsertCommand;
import dao.LggDao;
@Controller
public class LggController {
	private LggDao lggDao;
	private MultipartResolver multipartResolver;
	@Autowired
	public void setLggDao(LggDao lggDao) {
		this.lggDao = lggDao;
	}
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}
	@RequestMapping(value = "/attendance/subList", method = RequestMethod.GET)
	public String attendanceSubjectList(Model model) {
		int teacherId = 2;
		List<Subject> subjectList = lggDao.teachersSubject(teacherId);
		model.addAttribute("subjectList", subjectList );
		return "/attendance/attendanceSubList";
	}
	@RequestMapping(value = "/attendance/insert/{id}", method = RequestMethod.GET)
	public String attendanceInsert(@PathVariable("id") int subjectId, Model model) {
		List<Attendance> aList = lggDao.tempAttendanceList(subjectId);
		for (int i = 0; i < aList.size(); i++) {
			Attendance attendance = aList.get(i);
		}
		model.addAttribute("aList", aList );
		return "/attendance/attendanceInsert";
	}
	@RequestMapping(value = "/attendance/insert/{id}", method = RequestMethod.POST)
	public String attendanceInsertAction(AttendanceInsertCommand command,@PathVariable("id") int subjectId, Model model) {
		String[] tmpArr = command.getAttendanceCheck();
		int request = 0;
		if (tmpArr!=null) {
			request = lggDao.attendInsert(command,subjectId);
		}
		model.addAttribute("json", "{\"data\": "+request+"}");
		
		return "/ajax/ajaxDefault";
	}
	@RequestMapping(value = "/course/insert", method = RequestMethod.POST)
	public String subjectInsert(Subject command,
			Errors errors,//command 객체에 null이 포함가능할경우 반드시 써줄것
			Model model) {
		int rs = lggDao.subjectInsert(command);
		model.addAttribute("json", "{\"data\": "+rs+"}");
		return "/ajax/ajaxDefault";
	}
	@RequestMapping(value = "/course/edit", method = RequestMethod.POST)
	public String subjectEdit(Subject command,
			Errors errors,//command 객체에 null이 포함가능할경우 반드시 써줄것
			Model model) {
		int rs = lggDao.subjectEdit(command);
		model.addAttribute("json", "{\"data\": "+rs+"}");
		System.out.println(rs);
		return "/ajax/ajaxDefault";
	}
	@RequestMapping(value = "/course/delete")
	public String subjectDelete(int subject_id, Model model) {
		System.out.println(subject_id);
		int delOk = lggDao.subjectDelete(subject_id);
		model.addAttribute("json", "{\"data\": "+delOk+"}");
		return "/ajax/ajaxDefault";
	}

	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public String authDefault(Model model) {
		return "/admin/authList";
	}
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public String authList(@RequestParam(value="auth_ename") String auth_ename,Model model) {
		List<AuthMember> memberList = lggDao.authList(auth_ename);
		String json = "";
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			json = ow.writeValueAsString(memberList);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("json", "{\"data\": "+json+"}");
		return "/ajax/ajaxDefault";
	}
	@RequestMapping(value = "/auth/insert", method = RequestMethod.POST)
	public String authInsert(
			@RequestParam(value="m_id[]") List<Integer> m_ids,
			@RequestParam(value="auth_ename") String auth_ename,
			@RequestParam(value="auth_end_date",required=false) String auth_end_date,
			Model model) {
		int auth_manager_id = 1;//세션 대체
		int rs = lggDao.authInsert(m_ids,auth_ename,auth_manager_id);
		model.addAttribute("json", "{\"data\": "+rs+"}");
		return "/ajax/ajaxDefault";
	}
	@RequestMapping(value = "/auth/delete", method = RequestMethod.POST)
	public String authDelete(
			@RequestParam(value="m_id[]") List<Integer> m_ids,
			@RequestParam(value="auth_ename") String auth_ename,
			Model model) {
		int auth_manager_id = 1;//세션 대체
		int delOk = lggDao.authDelete(m_ids,auth_ename);
		model.addAttribute("json", "{\"data\": "+delOk+"}");
		return "/ajax/ajaxDefault";
	}

	//반-학생(선생)
	@RequestMapping(value = "/subJoinMem", method = RequestMethod.GET)
	public String subJoinMemDefault(Model model) {
		List<Subject> subjectList = lggDao.simpleSubjectList();
		model.addAttribute("subjectList",subjectList);
		return "/admin/subJoinMem";
	}
	
	@RequestMapping(value = "/subJoinMem", method = RequestMethod.POST)
	public String subJoinMemList(
			@RequestParam(value="subject_id") Integer subject_id,
			@RequestParam(value="auth_ename") String auth_ename,
			Model model) {
		List<SubJoinMem> memberList = lggDao.subJoinMemList(subject_id,auth_ename);
		String json = "";
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			json = ow.writeValueAsString(memberList);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("json", "{\"data\": "+json+"}");
		return "/ajax/ajaxDefault";
	}
	@RequestMapping(value = "/subJoinMem/insert", method = RequestMethod.POST)
	public String subJoinMemInsert(
			@RequestParam(value="m_id[]") List<Integer> m_ids,
			@RequestParam(value="subject_id") String subject_id,
			@RequestParam(value="auth_ename") String auth_ename,
			Model model) {
		int stuJoinSub_manager_id = 1;//세션 대체
		int rs = lggDao.subJoinMemInsert(m_ids,subject_id,auth_ename);
		model.addAttribute("json", "{\"data\": "+rs+"}");
		return "/ajax/ajaxDefault";
	}
	@RequestMapping(value = "/subJoinMem/delete", method = RequestMethod.POST)
	public String subJoinMemDelete(
			@RequestParam(value="m_id[]") List<Integer> m_ids,
			@RequestParam(value="subject_id") String subject_id,
			@RequestParam(value="auth_ename") String auth_ename,
			Model model) {
		int stuJoinSub_manager_id = 1;//세션 대체
		int delOk = lggDao.subJoinMemDelete(m_ids,subject_id,auth_ename);
		model.addAttribute("json", "{\"data\": "+delOk+"}");
		return "/ajax/ajaxDefault";
	}
	
}
