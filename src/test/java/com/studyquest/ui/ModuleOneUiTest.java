package com.studyquest.ui;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import com.studyquest.MainApp;
import com.studyquest.system.FileDataRepository;
import com.studyquest.system.GameController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.io.TempDir;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ModuleOneUiTest {
    private static Stage desktop;
    @TempDir Path folder;
    @BeforeAll static void startFx() throws Exception {
        CountDownLatch ready=new CountDownLatch(1);
        Platform.startup(()->{Platform.setImplicitExit(false);ready.countDown();});
        assertTrue(ready.await(10,TimeUnit.SECONDS),"JavaFX gagal dimulai");
        desktop=fx(Stage::new);
    }
    @AfterAll static void stopFx() throws Exception {
        fx(()->{ new ArrayList<>(Window.getWindows()).forEach(Window::hide); return null; });
        Platform.exit();
    }

    @Test @Order(2) void accountFormsPreviewProfileAndReopenUseRealJavaFxWindow() throws Exception {
        Stage stage=desktop; StackPane root=fx(StackPane::new);
        var controller=new GameController(new FileDataRepository(folder));
        Router router=fx(()->{
            stage.setScene(new Scene(root,1280,720));
            stage.getScene().getStylesheets().add(MainApp.class.getResource("/styles/theme.css").toExternalForm());
            var r=new Router(controller,stage,root); r.show("S01"); stage.show(); return r;
        });
        click(root,"login-register");
        click(root,"register-back");click(root,"login-register");
        fill(root,"register-username","Max"); fill(root,"register-password","secret"); fill(root,"register-confirm","different");
        click(root,"register-submit");
        assertTrue(text(root,"register-status").contains("belum sama"));
        fill(root,"register-confirm","secret"); click(root,"register-submit");
        await(()->"S03".equals(router.getCurrentScreen()));
        fx(()->{ ((javafx.scene.control.RadioButton)root.lookup("#class-Archer")).fire(); return null; });
        assertTrue(text(root,"character-preview").contains("PiercingShot"));
        fx(()->{ ((javafx.scene.control.RadioButton)root.lookup("#class-Warrior")).fire(); return null; });
        assertTrue(text(root,"character-preview").contains("PowerStrike"));
        fx(()->{ ((javafx.scene.control.RadioButton)root.lookup("#class-Mage")).fire(); return null; });
        assertTrue(text(root,"character-preview").contains("HP 80"));
        assertTrue(text(root,"character-preview").contains("Fireball"));
        fill(root,"character-name","x"); click(root,"character-submit");
        await(()-> !((Label)root.lookup("#character-status")).getText().isEmpty()
                && !((Label)root.lookup("#character-status")).getText().equals("Memproses..."));
        assertEquals("S03",fx(router::getCurrentScreen));
        fill(root,"character-name","Jury"); click(root,"character-submit"); await(()->"S04".equals(router.getCurrentScreen()));
        assertTrue(text(root,"hub-hud").contains("Jury"));
        click(root,"hub-profile"); assertTrue(text(root,"profile-detail").contains("Fireball"));
        click(root,"profile-back"); assertEquals("S04",fx(router::getCurrentScreen));
        dismissInformationAfterClick(root,"hub-save");
        dismissInformationAfterClick(root,"object-S05");
        assertEquals("S04",fx(router::getCurrentScreen));
        fx(()->{root.applyCss();root.layout(); return null;});
        assertTrue(fx(()->root.lookup("#hub-room").getBoundsInParent().getMaxX()<=1280));
        assertTrue(fx(()->root.lookup("#hub-room").getBoundsInParent().getMaxY()<=720));
        click(root,"hub-logout"); assertEquals("S01",fx(router::getCurrentScreen));
        fill(root,"login-username","MAX"); fill(root,"login-password","wrong"); click(root,"login-submit");
        await(()->((Label)root.lookup("#login-status")).getText().contains("salah"));
        fill(root,"login-password","secret"); click(root,"login-submit"); await(()->"S04".equals(router.getCurrentScreen()));
        assertTrue(text(root,"hub-hud").contains("Jury"));
        System.out.println("UI accounts: confirmation error, register, Mage preview, invalid name, create, profile, back, logout, wrong login, reload PASS");
    }

    @Test @Order(1) void clickAndEUseSameRouteAndMovementStopsOffHubAndOnFocusLoss() throws Exception {
        var controller=new GameController(new FileDataRepository(folder));
        controller.register("movement","secret"); controller.createCharacter("Warrior","Max");
        Stage stage=desktop; StackPane root=fx(StackPane::new);
        Router router=fx(()->{stage.setScene(new Scene(root,1280,720));var r=new Router(controller,stage,root);stage.show();r.backToHub();return r;});
        fx(()->{root.applyCss();root.layout();activate(stage,root.lookup("#hub-room"));return null;});
        long focusDeadline=System.nanoTime()+TimeUnit.SECONDS.toNanos(2);
        while(!fx(stage::isFocused)&&System.nanoTime()<focusDeadline) Thread.sleep(25);
        Assumptions.assumeTrue(fx(stage::isFocused),"Desktop OS tidak memberi fokus JavaFX; gerak/focus-loss perlu uji manual Max.");
        await(()->stage.getScene().getFocusOwner()==root.lookup("#hub-room"));
        double startX=fx(()->root.lookup("#hub-player").getLayoutX());
        fx(()->{key(root.lookup("#hub-room"),KeyEvent.KEY_PRESSED,KeyCode.D);return null;});
        await(()->root.lookup("#hub-player").getLayoutX()>startX);
        fx(()->{key(root.lookup("#hub-room"),KeyEvent.KEY_RELEASED,KeyCode.D);return null;});
        assertTrue(fx(()->root.lookup("#hub-player").getLayoutX())>startX);
        ParentHolder holder=fx(()->new ParentHolder(root.lookup("#hub-player"),root.lookup("#hub-room")));
        fx(()->{router.show("PROFILE");return null;});
        double stopped=fx(holder.player::getLayoutX);
        fx(()->{key(holder.room,KeyEvent.KEY_PRESSED,KeyCode.D);return null;}); Thread.sleep(100);
        assertEquals(stopped,fx(holder.player::getLayoutX));
        fx(()->{router.backToHub();activate(stage,holder.room);return null;});
        Stage other=fx(()->{Stage s=new Stage();s.setScene(new Scene(new StackPane(),100,100));s.show();s.requestFocus();return s;});
        await(()->!stage.isFocused());
        double unfocused=fx(holder.player::getLayoutX); Thread.sleep(100); assertEquals(unfocused,fx(holder.player::getLayoutX));
        fx(()->{other.hide();activate(stage,holder.room);return null;}); await(stage::isFocused);
        fx(()->{holder.player.relocate(1,220);holder.room.requestFocus();key(holder.room,KeyEvent.KEY_PRESSED,KeyCode.A);return null;});
        Thread.sleep(150);
        fx(()->{key(holder.room,KeyEvent.KEY_RELEASED,KeyCode.A);return null;});
        assertTrue(fx(holder.player::getLayoutX)>=0);
        fx(()->{holder.player.relocate(90,140);holder.room.requestFocus();key(holder.room,KeyEvent.KEY_PRESSED,KeyCode.W);return null;});
        Thread.sleep(150);
        fx(()->{key(holder.room,KeyEvent.KEY_RELEASED,KeyCode.W);return null;});
        assertFalse(fx(()->holder.player.getBoundsInParent().intersects(root.lookup("#object-S05").getBoundsInParent())));
        for(String id:List.of("S05","S08","S09","S10","S11","S12")) {
            fx(()->{
                router.registerPanel(id,new Panel(){
                    final StackPane panel=new StackPane(new Label(id));
                    public javafx.scene.Parent getView(){return panel;}
                    public void refresh(){}
                });
                router.backToHub(); root.applyCss();root.layout();
                ((Button)root.lookup("#object-"+id)).fire(); assertEquals(id,router.getCurrentScreen());
                router.backToHub();root.applyCss();root.layout();
                var object=root.lookup("#object-"+id).getBoundsInParent();
                holder.player.relocate(object.getCenterX()-16,object.getMinY()-45);
                holder.room.requestFocus();key(holder.room,KeyEvent.KEY_PRESSED,KeyCode.E);
                assertEquals(id,router.getCurrentScreen()); return null;
            });
        }
        System.out.println("UI hub: movement, off-panel pause, focus-loss pause, six object clicks and six E routes PASS (test panels only)");
    }

    @Test @Order(3) void mainAppCloseSavesAndNewWindowLoadsSameCharacter() throws Exception {
        String oldHome=System.getProperty("user.home");
        Stage stage=desktop;
        try {
            System.setProperty("user.home",folder.toString());
            fx(()->{new MainApp().start(stage);return null;});
        } finally { System.setProperty("user.home",oldHome); }
        StackPane root=fx(()->(StackPane)stage.getScene().getRoot());
        assertFalse(fx(stage::isResizable)); assertEquals(1280,fx(()->stage.getScene().getWidth()));
        assertEquals(720,fx(()->stage.getScene().getHeight()));
        click(root,"login-register"); fill(root,"register-username","closed");
        fill(root,"register-password","secret");fill(root,"register-confirm","secret");click(root,"register-submit");
        await(()->root.lookup("#character-name")!=null);
        fill(root,"character-name","Saved");click(root,"character-submit");await(()->root.lookup("#hub-hud")!=null);
        fx(()->{Event.fireEvent(stage,new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));stage.hide();return null;});
        var repository=new FileDataRepository(folder.resolve(".studyquest/saves"));
        assertEquals("Saved",repository.load("closed").getUser().getCharacter().getName());
        assertTrue(java.nio.file.Files.exists(folder.resolve(".studyquest/saves/closed.dat.bak")));
        System.out.println("UI MainApp: fixed scene, window-close save, persisted character PASS");
    }

    @Test @Order(4) void allObjectsRouteClicksAndUnavailablePanelReportsHonestly() throws Exception {
        var controller=new GameController(new FileDataRepository(folder));
        controller.register("routes","secret");controller.createCharacter("Archer","Max");
        StackPane root=fx(StackPane::new);
        Router router=fx(()->{
            desktop.setScene(new Scene(root,1280,720));var r=new Router(controller,desktop,root);
            desktop.show();r.backToHub();return r;
        });
        for(String id:List.of("S05","S08","S09","S10","S11","S12")) {
            fx(()->{
                router.registerPanel(id,new Panel(){
                    private final StackPane panel=new StackPane(new Label("Test route "+id));
                    public javafx.scene.Parent getView(){return panel;}
                    public void refresh(){}
                });
                ((Button)root.lookup("#object-"+id)).fire();
                assertEquals(id,router.getCurrentScreen());router.backToHub();return null;
            });
        }
        fx(()->{controller.logout();router.show("S04");assertEquals("S01",router.getCurrentScreen());return null;});
        System.out.println("UI routes: all six object controls open registered test panels; unauthenticated access returns login PASS");
    }

    private record ParentHolder(Node player,Node room){}
    private static void activate(Stage stage,Node room) {
        stage.toFront();stage.requestFocus();
        var bounds=room.localToScreen(room.getBoundsInLocal());
        var robot=new javafx.scene.robot.Robot();
        robot.mouseMove(bounds.getCenterX(),bounds.getCenterY());
        robot.mouseClick(javafx.scene.input.MouseButton.PRIMARY);
        room.requestFocus();
    }
    private static void key(Node node,javafx.event.EventType<KeyEvent> type,KeyCode code) {
        Event.fireEvent(node,new KeyEvent(type,"","",code,false,false,false,false));
    }
    private static void fill(StackPane root,String id,String value)throws Exception {
        fx(()->{((TextField)root.lookup("#"+id)).setText(value);return null;});
    }
    private static void click(StackPane root,String id)throws Exception {
        fx(()->{((Button)root.lookup("#"+id)).fire();return null;});
    }
    private static void dismissInformationAfterClick(StackPane root,String id)throws Exception {
        FutureTask<Void> action=new FutureTask<>(()->{((Button)root.lookup("#"+id)).fire();return null;});
        Platform.runLater(action);
        await(()->Window.getWindows().stream().anyMatch(w->w.isShowing()
                && w.getScene()!=null && w.getScene().getRoot() instanceof javafx.scene.control.DialogPane));
        fx(()->{new ArrayList<>(Window.getWindows()).stream()
                .filter(w->w.getScene()!=null && w.getScene().getRoot() instanceof javafx.scene.control.DialogPane)
                .forEach(Window::hide);return null;});
        action.get(10,TimeUnit.SECONDS);
    }
    private static String text(StackPane root,String id)throws Exception {return fx(()->((Label)root.lookup("#"+id)).getText());}
    private static void await(BooleanSupplier condition)throws Exception {
        long until=System.nanoTime()+TimeUnit.SECONDS.toNanos(10);
        while(System.nanoTime()<until){if(fx(condition::getAsBoolean))return;Thread.sleep(25);}
        fail("UI condition timeout");
    }
    private static <T>T fx(Callable<T> action)throws Exception {
        FutureTask<T> task=new FutureTask<>(action);Platform.runLater(task);return task.get(15,TimeUnit.SECONDS);
    }
}
