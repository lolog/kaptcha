## 1. Description
spring graphic verification

# 2. Dependency
* spring-4.2-xxx.jar
* kaptcha-2.3.2.jar

## 3. Options
```javascript
kaptcha.border   --------------------border,               default: yes,       options: yes,no  
kaptcha.border.color   --------------border color,         default: Color.BLACK  
kaptcha.border.thickness  -----------border width,         default: 1  
kaptcha.producer.impl   -------------charactor generator,  default: DefaultKaptcha  
kaptcha.textproducer.impl   ---------charactor generator,  default: DefaultTextCreator  
kaptcha.textproducer.char.string ----charactor charactor,  default: abcde2345678gfynmnpwx  
kaptcha.textproducer.char.length ----charactor length,     default: 5  
kaptcha.textproducer.font.names  ----font,  default: new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)  
kaptcha.textproducer.font.size   ----font size,                default: 40  
kaptcha.textproducer.font.color  ----charactor color,          default: Color.BLACK  
kaptcha.textproducer.char.space  ----charactor spacing ,       default: 2  
kaptcha.noise.impl   ----------------noise generation,         default: DefaultNoise  
kaptcha.noise.color  ----------------noise color,              default: Color.BLACK  
kaptcha.obscurificator.impl ---------style engine,             default: WaterRipple  
kaptcha.word.impl   -----------------charactor rendering,      default: DefaultWordRenderer  
kaptcha.background.impl   -----------background generator,     default: DefaultBackground  
kaptcha.background.clear.from  ------gradual background color, default: Color.LIGHT_GRAY  
kaptcha.background.clear.to  --------gradual background color, default: Color.WHITE  
kaptcha.image.width   ---------------image width,              default: 200  
kaptcha.image.height  ---------------image height,             default: 50
```

## 4. Configure
### 4.1. web.xml
```xml
<servlet>
    <servlet-name>kaptcha</servlet-name>
    <servlet-class>com.google.code.kaptcha.servlet.KaptchaServlet</servlet-class>  
</servlet>
<servlet-mapping>
    <servlet-name>kaptcha</servlet-name>  
    <url-pattern>*.jpg</url-pattern>
</servlet-mapping>
```
Or:
```xml
<servlet>  
    <servlet-name>Kaptcha</servlet-name>  
    <servlet-class>com.google.code.kaptcha.servlet.KaptchaServlet</servlet-class>  
    <init-param>  
        <param-name>kaptcha.image.width</param-name>  
        <param-value>200</param-value>  
    </init-param>  
    <init-param>  
        <param-name>kaptcha.image.height</param-name>  
        <param-value>50</param-value>  
    </init-param>  
    <init-param>  
        <param-name>kaptcha.textproducer.char.length</param-name>  
        <param-value>4</param-value>  
    </init-param>  
    <init-param>  
        <param-name>kaptcha.noise.impl</param-name>  
        <param-value>com.google.code.kaptcha.impl.NoNoise</param-value>  
    </init-param>  
</servlet>
<servlet-mapping>
    <servlet-name>kaptcha</servlet-name>  
    <url-pattern>*.jpg</url-pattern>
</servlet-mapping>
```
### 4.2. kaptcha.xml
```xml
<!-- kaptcha config -->
<bean id="kaptchaConfig" class="com.google.code.kaptcha.util.Config">  
	<constructor-arg>  
		<props>
			<prop key="kaptcha.border">yes</prop>
			<prop key="kaptcha.border.color">105,179,90</prop>
			<prop key="kaptcha.textproducer.font.color">blue</prop>
			<prop key="kaptcha.image.width">125</prop>
			<prop key="kaptcha.image.height">45</prop>
			<prop key="kaptcha.textproducer.font.size">45</prop>
			<prop key="kaptcha.session.key">code</prop>
			<prop key="kaptcha.textproducer.char.length">4</prop>
			<prop key="kaptcha.textproducer.font.names">宋体,楷体,微软雅黑</prop>
		</props>
	</constructor-arg>
</bean>
<bean id="captchaProducer" class="com.google.code.kaptcha.impl.DefaultKaptcha">  
<property name="config" ref="kaptchaConfig"/>
</bean>
```
## 5. Controller
```java
@Controller
@RequestMapping("/kaptcha")
public class KaptchaController {
      
    @Autowired
    private Producer captchaProducer = null;
  
    @RequestMapping(value = "authority")
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String      code    = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);  
        
        // header setting
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        
        // create and save verify code
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        
        BufferedImage       bi  = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }  
}
```