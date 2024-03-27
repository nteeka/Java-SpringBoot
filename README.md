Website Studyroom - xây dựng dựa theo Google Classroom

- Cách chạy bài :
    Cài đặt xampp -> bật phpadmin và mysql 
    Tạo database tên Demo bằng xampp
    chỉnh sửa java jdk tương thích (hình như em-mình đang sử dụng SDK 21)
    Chạy project 
    truy cập http://localhost:8080/Home/login để bắt đầu
 
Mô tả những gì đã có (vẫn đang trong quá trình phát triển và hoàn thiện) :


    - Đăng nhập : email và password, validate(nếu sai 1 trong hai trường).Lưu phiên đăng nhập bằng session.
    - Đăng ký : validate(email, password>6 và phải có ký tự hoa và thường), save password đã mã hóa.
    - Change password : nhập đúng mk hiện tại -> lưa mk mới đã nhập và lưu với mk đã được mã hóa.
    - Quên mật khẩu : gửi token về email (email sandbox qua MailTrap) có validate về time expired và sự chính xác của token.
    - ...

    - Class : 
        Create : lớp được tạo bởi một tài khoản, sinh ra một mã classId là chuỗi String ngẫu nhiên 6  ký tự bao gồm chữ lẫn số.

        Join : các account khác tham gia lớp với mã lớp được người tạo lớp gửi cho (có thể liên lạc bên ngoài), tham gia qua tìm kiếm mã lớp(nhập mã) kết hợp validate(nếu nhập sai mã sẽ thông báo lớp đó không tồn tại).

        Leave : out class, account đã tham gia lớp muốn rời khỏi.

        List : Sẽ hiện thị danh sách class mà account đang trong session login đã tham gia và chưa leave.

    - Notification : là thông báo của người tạo lớp - hoặc giáo viên (chưa rõ ràng trong role và nghiệp vụ nên chưa thể khẳng định) đến với mọi người đã tham gia lớp đó.Có thể đính kèm một file để gửi cho các thành viên trong class.
    

    - Comment : comment cho từng notification, ghi nhận lại thông tin người viết cmt, ngày tháng năm tạo và modified - delete(dành riêng cho người đã viết - tạo cmt đó).


    - Homework : được người tạo class tạo ra dành cho những thành viên trong class để làm bài tập. Có dữ liệu về time(deadline), file đính kèm(nội dung bài tập), description (mô tả bài tập).
        - Bao gồm các hành động như :
            + Đăng bài tập (create - update time deadline)
            + Nộp bài tập (các thành viên nộp bài - hiện tại chưa xác định rõ nghiệp vụ - sẽ update trong time sau).
            + Deadline : khi thời gian thực chạm ngưỡng và vượt qua deadline thì cổng nộp bài sẽ tự động đóng và không cho các thành viên nộp bài
            + Status : bao gồm các trạng thái như đã nộp - chưa nộp  và nộp trễ (nếu người tạo setting cho phép thành viên nộp sau deadline).
            + Biến : là tên một biến gì đó để cho người tạo class setting 1 0 - true false cho phép thành viên nộp sau deadline hoặc block
            


    - Account : 
        - Sau này sẽ update thêm những tính năng như :
            +   Quản lý tài khoản cá nhân
            +   Điều chỉnh giao diện (sáng tối)
            +...
    
    - Phân quyền :
        - Sẽ xác định rõ nghiệp vụ và các role - phân quyền và triển khai author - authen hợp lý để hạn chế - ẩn đi những chức năng mà account k đủ quyền để sử dụng.


    - Validate : hiện tại đang thiếu validate cho một số chức năng và model như homework và noti, sẽ bổ sung và hoàn thiện sau.


    <3 Nguyễn Tường Khang <3

    Like-unlike cmt
        - Show list comment đã được account in session like => liked(wait for unlike) nếu chưa like thì hiện nút like(for likeComment).
        - Action like mượt - prevent reload phải load
        
    Account :
        - Hoàn thiện tác vụ lưu file (file ảnh account) : lưu với 1 path unique => để replace existing không tốn nhiều dung lượng (khác hình nhưng vẫn cùng tên)
        - Manager account (edit account by user)
        
    - Multiple file
        - Tạo bảng FileAttach kết nối liên kết với HW và Noti để có thể gửi được nhiều file một lúc
        - Đã tối ưu - phân biệt tên file dựa trên id và loại file (tên file = id(hw hoặc noti)_(hw hoặc noti)_tên nguyên thủy)
        - Đã modify xong create Noti
    - Reply comment
        - tạo bảng reply
        - thao tác chưa mượt
        - Chuẩn bị cho like - edit - delete

    - Submit homework
        - Đã nộp được bài (member)
        - phân biệt đã nộp - chưa nộp
        - phân biệt dealine trễ và còn hạn (trễ - > close, còn hạn hoặc không có hạn nộp -> open)
        - Block submit khi đã quá hạn (deadline before now).
        - Giao diện xấu - Chưa tách biệt creater and member (fuction used in submit)

    - Homework :
        Set deadline (or not) for hw(dealine allway before now).
        Lọc hw chưa nộp - đã nộp - tất cả


WHAT IS COMING :

    
    - doing
        validate mọi thứ (hw, noti, cmt,...)
        search class (already join)
        Like - Edit - Delete for Reply comment
        Smooth createComment - createReply
        edit hw with file
        xem lại thông tin bài tập đã nộp (member)
        Giao diện chi tiết bài tập
        Giao diện và backend xử lý việc nộp bài(hiển thị danh sách member nộp bài)
        Chấm điểm( class creater) - bảng điểm member
        Hủy nộp bài - edit bài nộp
        Phân chia quyền hạn tốt hơn - tối ưu hơn trong Submit and list homework
        Quản lý member trong class(view list - filter - search,...)
        Thông báo (Member) khi có bài tập - thông báo mới từ class creater
    
    - Advance - will try
        Complete layout
        Clear dư thừa - tối ưu code (DRY)       
        Xem xét nâng cấp lưu file trên một phương tiện nền tảng khác(ggdrive, cloud, ....)
        gửi mail real( không còn gửi sandbox qua mail trap)
    

