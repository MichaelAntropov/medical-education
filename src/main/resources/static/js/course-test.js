
const host = window.location.origin;

const courseId = document.getElementById("courseId").value;

const testId = document.getElementById("testId").value;

const nextItemId = document.getElementById("nextItemId").value;

const nextItemType = document.getElementById("nextItemType").value;

const questionListElement = document.getElementById("questionListElement");

const receiveQuestions = receiveTestQuestions();

const testQuestions = [];

const userAnswers = [];

const csrf = document.getElementsByName("_csrf").item(0).value;

async function receiveTestQuestions() {

    let responseJson;

    try {
        let response = await
            fetch(`${host}/courses/course${courseId}/test${testId}/questions`, {
                method: "GET"
            });

        if (!response.ok) {
            mainInfoShowAlert("Error - " + response.status + ".")
            return;
        }

        responseJson = await response.json();
    } catch (err) {
        mainInfoShowAlert(err);
        return;
    }

    let questions = responseJson.questions;

    if(questions.length > 0) {
        questions.forEach(question => {
            testQuestions.push(question);
        })
        questionListElement.innerHTML = "";
        initUserAnswers();
        renderQuestions(questions);
    }

}

function mainInfoShowAlert(message) {
    let mainInfoAlertsElement = document.getElementById("mainInfoAlertsElement");
    let alertId = getRandomInt(0, 2147483647);
    let alert = createInfoAlertNode(message, alertId);
    mainInfoAlertsElement.appendChild(alert);
}

function createInfoAlertNode(message, alertId) {
    return htmlToElement(
        "<div id=\"mainInfoAlert" + alertId + "\" class=\"alert alert-primary alert-dismissible fade show\" role=\"alert\"\n" +
        "     >\n" +
        "    <div id=\"mainInfoAlertText\">" + message + "</div>\n" +
        "    <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" aria-label=\"Close\">\n" +
        "        <span aria-hidden=\"true\">&times;</span>\n" +
        "    </button>\n" +
        "</div>"
    );
}

function renderQuestions(questions) {
    questions.forEach((question) => {
        let questionNode = getQuestionNode(question);
        questionListElement.appendChild(questionNode);
        let questionAnswersElement = document.getElementById("questionAnswersElement" + question.questionId);
        question.answers.forEach((answer) => {
            let answerNode = getAnswerNode(question, answer);
            questionAnswersElement.appendChild(answerNode);
        })
    })
}

function getQuestionNode(question) {
    return htmlToElement(
        "<div id=\"questionElement\" class=\"card card-body mb-3\">\n" +
        "        <div class=\"container d-inline-flex\">\n" +
        "            <div class=\"mr-2\">" + question.orderNumber + ".</div>\n" +
        "            <div id=\"questionTextElement\" class=\"container\">" + question.content + "</div>\n" +
        "        </div>\n" +
        "\n" +
        "       <div id=\"questionAnswersElement" + question.questionId + "\" class=\"container m-3\"></div>\n" +
        "    </div>\n" +
        "</div>"
    );
}

function getAnswerNode(question, answer) {
    return htmlToElement(
        "<div class=\"custom-control custom-radio\" onclick=\"answerQuestion(" + question.questionId + "," +  answer.answerId + ")\">\n" +
        "     <input type=\"radio\" id=\"answerRadio" + answer.answerId + "\" name=\"answerRadio" + question.questionId + "\" class=\"custom-control-input\">\n" +
        "     <label class=\"custom-control-label\" for=\"answerRadio" + answer.answerId + "\">\n" +
        "        <div class=\"container\">" + answer.content + "</div>\n" +
        "    </label>\n" +
        "</div>"
    );
}
//Record answers and send them
////////////////////////////////////////////////////

class UserAnswer {
    questionId;
    answerId;

    constructor(questionId, answerId) {
        this.questionId = questionId;
        this.answerId = answerId;
    }
}

class PostUserAnswer {
    courseId;
    testId;
    userAnswers;

    constructor(courseId, testId, userAnswers) {
        this.courseId = courseId;
        this.testId = testId;
        this.userAnswers = userAnswers;
    }
}

function initUserAnswers() {

    testQuestions.forEach(question => {
        userAnswers.push(new UserAnswer(question.questionId, null));
    })
}

function answerQuestion(questionId, answerId) {
    userAnswers.find(userAnswer =>
        userAnswer.questionId === questionId).answerId = answerId;
}

async function postTestAnswers() {

    try {
        let data = JSON.stringify(new PostUserAnswer(courseId, testId, userAnswers));


        let response = await fetch(`${host}/courses/course/test/submit`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN' : csrf,
            },
            body: data
        });

        if (!response.ok) {
            mainInfoShowAlert("Error - " + response.status + ".")
            return;
        }

        if(response.ok) {
            window.location.reload(true);
        }
    } catch (err) {
        mainInfoShowAlert(err.message);
    }
}

///////////////
///////////////
///////////////
//Utility functions
////////////////////////////////////////////////////
function htmlToElement(html) {
    let template = document.createElement('template');
    html = html.trim(); // Never return a text node of whitespace as the result
    template.innerHTML = html;
    return template.content.firstChild;
}

function getRandomInt(min, max) {
    return Math.random() * (max - min) + min;
}