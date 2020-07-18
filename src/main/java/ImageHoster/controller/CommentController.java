package ImageHoster.controller;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;


    /**
     * Add comment to a pic
     *
     * @param imageId    Imageid selected by user
     * @param imageTitle Title of the image
     * @param comment    Comment passed by user
     * @param session    Session of logged in user
     * @return html page to view code
     */
    @RequestMapping(value = "/image/{imageId}/{imageTitle}/comments", method = RequestMethod.POST)
    public String addComment(
            @PathVariable(name = "imageId") Integer imageId,
            @PathVariable(name = "imageTitle") String imageTitle,
            @RequestParam String comment,
            HttpSession session) {
        Image image = imageService.getImage(imageId);
        Comment userComment = new Comment();
        userComment.setText(comment);
        userComment.setImage(image);
        User loggedInUser = getLoggedInUser(session);
        userComment.setUser(loggedInUser);
        userComment.setCreatedDate(new Date());
        // Add comment
        commentService.createComment(userComment);
        return "redirect:/images/" + imageId + "/" + imageTitle;
    }

    private User getLoggedInUser(HttpSession session) {
        Object loggedUser = session.getAttribute("loggeduser");
        if (loggedUser == null) {
            return null;
        }
        return (User) loggedUser;
    }
}
